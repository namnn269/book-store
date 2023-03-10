package com.nam.controller;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.ErrorMsgDto;
import com.nam.dto.UserRegistrationFormDto;
import com.nam.entity.RegistrationToken;
import com.nam.entity.User;
import com.nam.event.RegistrationCompletionEvent;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ValidFormException;
import com.nam.repository.IRegistraionTokenRepository;
import com.nam.service.IUserService;
import com.nam.utils.UrlFromUser;

@Controller
@RequestMapping("/register")
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
public class RegistrationController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IRegistraionTokenRepository tokenRepo;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private Environment env;

	/* Hiển thị form đăng ký người dùng */
	@GetMapping("/form")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new UserRegistrationFormDto());
		return "signin-up/signupform";
	}

	/* Nhận vào thông tin đăng ký và thực hiện lưu người dùng */
	@PostMapping("/save")
	public ModelAndView registerNewUser(@ModelAttribute UserRegistrationFormDto userRegDto, HttpServletRequest http) {
		ModelAndView mav = new ModelAndView();
		try {
			User user = (User) userService.saveNewRegisterUser(userRegDto).get();
			String url = UrlFromUser.getUrl(http);
			publisher.publishEvent(new RegistrationCompletionEvent(user, http.getLocale(), url));
			String message = env.getProperty("message.confirm.email.notification")
							+ Long.parseLong(env.getProperty("registration.expiration"))/60
							+ env.getProperty("message.minute");
			mav.addObject("message", message);
			mav.setViewName("signin-up/loginform");
		} catch (ObjectAlreadyExistedException | ValidFormException p) {
			ErrorMsgDto msg = new ErrorMsgDto(p.getMessage());
			mav.setViewName("signin-up/signupform");
			mav.addObject("user", userRegDto);
			mav.addObject("errorMsg", msg);
		} catch (Exception e) {
			mav = new ModelAndView("view/error/common-error");
			mav.addObject("errorMsg", new ErrorMsgDto("Cannot save!"));
		}
		return mav;
	}
	
	/* Nhận vào chuỗi token từ đường link có trong email để xác thực người dùng */
	@GetMapping(value = "/email-confirm")
	public String handleConfirmationEmailToken(@RequestParam("token") String tokenStr, HttpServletRequest http,
			Model model) {
		RegistrationToken token = tokenRepo.findByToken(tokenStr);
		// mã token sai
		if (token == null) 
			return "signin-up/error-token";

		if (token.getExpirationDate().after(new Timestamp(Calendar.getInstance().getTimeInMillis()))) {
			User user = token.getUser();
			user.setEnabled(true);
			tokenRepo.save(token);
			return "signin-up/success";
		} 
		// token đã quá hạn
		else {
			String url = UrlFromUser.getUrl(http) + "/register/expiration-token" + "?token=" + token.getToken();
			model.addAttribute("url", url);
			return "signin-up/expired-email-token";
		}

	}

	/* Nếu token bị quá hạn thì phương thức này sẽ thực hiện gửi lại email để xác thực lại */
	@GetMapping(value = "/expiration-token")
	public String handleExpiredToken(@RequestParam("token") String tokenStr, HttpServletRequest http, Model model) {
		RegistrationToken token = tokenRepo.findByToken(tokenStr);
		// mã token sai
		if (token == null) 
			return "signin-up/error-token";
		String url = UrlFromUser.getUrl(http);
		token.resetToken();
		publisher.publishEvent(new RegistrationCompletionEvent(token.getUser(), http.getLocale(), url));
		String message = env.getProperty("message.confirm.email.notification")
				+ Long.parseLong(env.getProperty("registration.expiration"))/60
				+ env.getProperty("message.minute");
		model.addAttribute("message", message);
		return "signin-up/loginform";
	}
}











