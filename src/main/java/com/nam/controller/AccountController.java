package com.nam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nam.dto.ProfileUserDto;
import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.ProfileUser;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ValidFormException;
import com.nam.service.IOrderDetailService;
import com.nam.service.IProfileService;
import com.nam.service.IUserService;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

	@Autowired
	IProfileService profileService;
	@Autowired
	IOrderDetailService orderDetailService;
	@Autowired
	IUserService userService;

	/* Hiển thị trang thông tin cá nhân người dùng gồm profile người dùng và các order đã mua */
	@GetMapping(value = "/my-account")
	public ModelAndView showAccount(@ModelAttribute("message") String message, @ModelAttribute("error") String error) {
		ModelAndView mav = new ModelAndView("view/user/my-account");
		List<PurchasedOrderDetailDto> purchasedBooks=orderDetailService.getListPurchasedOrderDetailDtos();
		ProfileUserDto profileUserDto = new ProfileUserDto();
		try {
			profileUserDto = profileService.getProfileDto();
			mav.addObject("profile", profileUserDto);
			mav.addObject("username", profileUserDto.getUsername());
			mav.addObject("purchased", purchasedBooks);
			mav.addObject("message", message.equals("") ? null : message);
			mav.addObject("error", error.equals("") ? null : error);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	/* Hiển thị form để sửa thông tin người dùng */
	@GetMapping(value = "/form-info")
	public ModelAndView showFormUpdateProfile() {
		ModelAndView mav = new ModelAndView("view/user/form-profile");
		try {
			ProfileUser profile = profileService.getProfile();
			mav.addObject("profile", profile);
			mav.addObject("username", userService.getCurrentLoggedInUser().getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	/* Thực hiện cập nhật lại thông tin người dùng từ form đã nhập và chuyển tiếp về trang my-account */
	@PostMapping(value = "/update-info")
	public ModelAndView updateProfile(@ModelAttribute ProfileUser profileUser, RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("redirect:/account/my-account");
		try {
			Message message = profileService.save(profileUser);
			ra.addFlashAttribute("message", message.getContent());
		} catch (ValidFormException f) {
			ra.addFlashAttribute("error", f.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	/* Hiển thị form để thay đổi mật khẩu người dùng */
	@GetMapping(value = "/form-change-password")
	public ModelAndView changePasswordForm() {
		ModelAndView mav=new ModelAndView("view/user/form-change-password");
		return mav;
	}
	
	/* Thực hiện thay đổi mật khẩu của người dung và chuyển tiếp về trang my-account */
	@PostMapping(value = "/change-password")
	public ModelAndView changePassword(	@RequestParam(value = "oldPassword") String oldPassword,
										@RequestParam(value = "newPassword1") String newPassword1,
										@RequestParam(value = "newPassword2") String newPassword2,
										RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("view/user/form-change-password");
		try {
			Message message = userService.changePassword(oldPassword, newPassword1, newPassword2);
			ra.addFlashAttribute("message",message.getContent());
			mav.setViewName("redirect:/account/my-account");
		} catch (ValidFormException f) {
			mav.addObject("errormsg",f.getMessage());
		} catch (Exception e) {
		}
		return mav;
	}
	


}





















