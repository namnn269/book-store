package com.nam.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.DisplayBookDto;
import com.nam.entity.Book;
import com.nam.entity.Category;
import com.nam.entity.ResetPasswordToken;
import com.nam.entity.User;
import com.nam.event.ResetPasswordEvent;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.repository.IResetPasswordTokenRepository;
import com.nam.service.IBookService;
import com.nam.service.ICategoryService;
import com.nam.service.IUserService;
import com.nam.utils.Constants;
import com.nam.utils.UrlFromUser;

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
@RequestMapping(value = {"/trang-chu","/"})
public class HomeController {
	@Autowired
	private IBookService bookService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IResetPasswordTokenRepository resetPasswordTokenRepo;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private Environment env;
	
	private String domain = Constants.DOMAIN;

	/* hi???n th??? trang ch??? c???a web */
	@GetMapping(value = { "" })
	public String home(Model model) {
		
		List<DisplayBookDto> bestSellerBooks=bookService.findBestSellerBooks();
		List<Category> categories= categoryService.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("bestSellerBooks", bestSellerBooks);
		
		User user;
		try {
			user = userService.getCurrentLoggedInUser();
			model.addAttribute("username", user.getUsername());
		} catch (Exception e) {
			user = null;
		}
		return "view/user/shop-product-list";
	}

	@GetMapping(value = { "/error-page" })
	public String errorPage() {
		return "view/error/error-page";
	}

	/* h??m tr??? v??? m???ng HTML 2 ph???n t??? d???a tr??n tham s??? nh???n v??o 
	 * ph???n t??? HTML 1 l?? n???i dung c???a t???p h???p th??? card
	 * ph???n t??? HTML 2 l?? ph???n pagination */
	@GetMapping(value = "/product-home-ajax")
	@ResponseBody
	public String[] callProductHomeAjax(
			@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
			@RequestParam(value = "searchFor", defaultValue = "") String searchFor,
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "sortbyPrice", defaultValue = "") String sortByPrice) {
		List<DisplayBookDto> books = bookService.findAll(pageNo, pageSize, searchFor, categoryId, sortByPrice);

		if(books.isEmpty())
			return new String[] { "<div class='alert alert-warning'>Kh??ng t??m th???y s???n ph???m n??o!</div>"};
		
		StringBuilder html = new StringBuilder("");
		for (DisplayBookDto bookDto : books) {
			html.append(getBookItemHtml(bookDto));
		}
		String paginationHtml = getPaginationHtml(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		return new String[] { html.toString(), paginationHtml };
	}
	
	
	/* hi???n th??? trang ????? xem chi ti???t 1 cu???n s??ch d???a v??o id nh???n ???????c */
	@GetMapping(value = "/book-detail/{id}")
	public ModelAndView showBookDetail(	@PathVariable("id") Long id,
										@ModelAttribute("msg") String message) {
		
		List<DisplayBookDto> bestSellerBooks = bookService.findBestSellerBooks();
		ModelAndView mav = new ModelAndView("view/user/book-detail");
		mav.addObject("bestSellerBooks", bestSellerBooks);
		User user = null;
		try {
			DisplayBookDto bookDto = bookService.findBookDetailById(id);
			mav.addObject("book", bookDto);
			user = userService.getCurrentLoggedInUser();
			if (user != null)
				mav.addObject("username", user.getUsername());
			if (!message.equals(""))
				mav.addObject("message", message);
		} catch (ObjectNotFoundException o) {
			mav.addObject("error_book_not_found", o.getMessage());
		} catch (Exception e) {
			mav.addObject("error_book_not_found", env.getProperty("message.common.undefined.error"));
			e.printStackTrace();
		}
		return mav;
	}


	/* ph????ng th???c tr??? v??? chu???i HTML c???a 1 ph???n item hi???n th??? tr??n trang ch??? d???a tr??n thu???c t??nh 1 cu???n s??ch */
	private String getBookItemHtml(DisplayBookDto bookDto) {
		String linkDetail = domain+"/trang-chu/book-detail/" + bookDto.getId() + "";
		String authors = "";
		for (String author : bookDto.getAuthors()) {
			authors += "<br>- " + author;
		}

		String html="<div class='col-md-4 col-sm-6 col-xs-12'>"
				+ "                <div class='product-item'>"
				+ "                  <div class='item1'>"
				+ "                      <a href='"+linkDetail+"'>"
				+ "                    		<img"
				+ "                      	 src='"+bookDto.getImgLink()+"'"
				+ "                     	 class='img-item'"
				+ "                     	 alt='"+bookDto.getBookTitle()+"'"
				+ "                   		 />"
				+ "                      </a>"
				+ "                  </div>"
				+ "                  <h3>"
				+ "                    <a style='font-weight: bold'>"+bookDto.getBookTitle()+"</a>"
				+ "                  </h3>"
				+ "                  <div>Th??? lo???i: "+bookDto.getCategory()+"</div>"
				+ "                  <br>"
				+ "                  <div>T??c gi???: "+authors+"</div>"
				+ "                  <br></br>"
				+ "                  <div class='pi-price'>"+bookDto.getPrice()+"??</div>"
				+ "                  </div>"
				+ "                </div>"
				+ "              </div>";
		return html;
	}

	/* ph????ng th???c tr??? v??? chu???i HTML c???a ph???n pagination d???a theo tham s??? truy???n v??o
	 * pageNo: trang s??? m???y, pageSize: m???y ph???n t??? 1 trang, searchFor: t??m ki???m theo c??i g??,
	 * categoryId: th??? lo???i c???a s??ch, sortByPrice: s???p x???p s??ch theo gi?? */
	private  String getPaginationHtml(int pageNo, int pageSize, String searchFor, long categoryId, String sortByPrice) {
		Page<Book> bookPage = bookService.getPageBook(pageNo, pageSize, searchFor, categoryId, sortByPrice);
		String html =			"<div class='hint-text'>"
				+ "                T???ng"
				+ "                <b>"+bookPage.getTotalElements()+"</b> s??ch"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#home-search-input' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= bookPage.getTotalPages(); i++) {
			html += "                  <li class='page-item " + (i == pageNo + 1 ? "active":" ")+"'>"
				+ "                    <a href='#home-search-input' class='page-link'>"+i+"</a>"
				+ "                  </li>";
		}
				
				html+=
				 "                <li class='next-page-item'>"
				+ "                  <a href='#home-search-input' class='page-link'>Next</a>"
				+ "                </li>"
				+ "              </ul>";
		
		return html;
	}
	
	/* hi???n th??? form ????? l???y th??ng tin username v?? email ????? reset password */
	@GetMapping(value = "/form-reset-password")
	public ModelAndView resetPasswordForm() {
		ModelAndView mav = new ModelAndView("view/user/form-reset-password");
		return mav;
	}
	
	/* t??? form g???i l??n th??ng tin username v?? email ????? th???c hi???n reser password */
	@PostMapping(value = "/reset-password")
	public ModelAndView resetPassword(	@RequestParam("username") String username,
										@RequestParam("email") String email,
										HttpServletRequest http) {
		ModelAndView mav = new ModelAndView("signin-up/loginform");
		try {
			Message message = userService.sendEmailToConfirmBeforeResetPassowrd(username, email, http);
			mav.addObject("message", message.getContent());
		} catch (ObjectNotFoundException o) {
			mav.setViewName("view/user/form-reset-password");
			mav.addObject("errormsg", o.getMessage());
		} catch (Exception e) {
		}
		return mav;
	}
	
	/* khi user click v??o link x??c th???c trong email s??? ki???m tra token
	 * tr?????ng h???p token b??? sai s??? tr??? v??? 1 trang l???i
	 * tr?????ng h???p token h???t h???n s??? tr??? v??? 1 trang c?? ch???a URL m???i ????? x??c th???c l???i
	 * tr?????ng h???p token h???p l??? s??? tr??? v??? trang x??c th???c th??nh c??ng v?? th???c hi???n reset password */
	@GetMapping(value = "/email-confirm-reset-password")
	public ModelAndView confirmResetPasswordToken(@RequestParam(value = "token") String token,
			HttpServletRequest http) {
		ModelAndView mav = new ModelAndView();

		Optional<ResetPasswordToken> opResetPasswordToken = resetPasswordTokenRepo.findByToken(token);
		// m?? token b??? sai
		if (opResetPasswordToken.isEmpty())
			mav.setViewName("signin-up/error-token");
		else {
			ResetPasswordToken passwordToken = opResetPasswordToken.get();
			// m?? token qu?? h???n
			if (passwordToken.getExpirationDate().before(new Timestamp(Calendar.getInstance().getTimeInMillis()))) {
				String url = UrlFromUser.getUrl(http) + "/trang-chu/expiration-token?token=" + token;
				mav.addObject("url", url);
				mav.setViewName("signin-up/expired-email-token");
			} else {
				mav.setViewName("signin-up/success");
				User user = passwordToken.getUser();
				Message message = userService.resetPassword(user);
				mav.addObject("message", message.getContent());
			}
		}
		return mav;
	}

	/* trong tr?????ng h???p token b??? qu?? h???n th?? g???i ?????n ph????ng th???c n??y
	 * l???y th??ng tin user t??? token g???i l??n v?? publish l???i 1 event resetpassword */
	@GetMapping(value = "/expiration-token")
	public ModelAndView handleExpiredToken(@RequestParam(value = "token") String token, HttpServletRequest http) {
		ModelAndView mav = new ModelAndView();
		Optional<ResetPasswordToken> opResetPasswordToken = resetPasswordTokenRepo.findByToken(token);
		if (opResetPasswordToken.isEmpty()) {
			mav.setViewName("signin-up/error-token");
			return mav;
		}
		ResetPasswordToken passwordToken = opResetPasswordToken.get();
		User user = passwordToken.getUser();

		publisher.publishEvent(new ResetPasswordEvent(user, UrlFromUser.getUrl(http)));
		String message=env.getProperty("message.confirm.email.notification")
				+ passwordToken.getEXPIRATION()/60
				+ env.getProperty("message.minute");
		mav.addObject("message",message);
		mav.setViewName("signin-up/expired-email-token");
		return mav;
	}
	
}













