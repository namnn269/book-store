package com.nam.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nam.dto.DisplayBookDto;
import com.nam.dto.ProfileUserDto;
import com.nam.entity.Book;
import com.nam.entity.Order;
import com.nam.entity.OrderDetail;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.OrderFailureException;
import com.nam.exception_mesage.OverQuantityException;
import com.nam.mapper.impl.BookMapper;
import com.nam.service.IOrderUserService;
import com.nam.service.IProfileService;
import com.nam.service.IUserService;
import com.nam.utils.Constants;

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
@RequestMapping(value = "/cart")
public class UserCurrentCartController {
	
	@Autowired
	private IOrderUserService orderUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProfileService profileService;
	@Autowired
	private Environment env;
	@Autowired
	private BookMapper mapperBook;
	
	private String domain = Constants.DOMAIN;

	private NumberFormat numberFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));

	/* Nh???n v??o ID s??ch v?? s??? l?????ng ????? th??m v??o gi??? h??ng ng?????i d??ng hi???n t???i */
	@PostMapping(value = "/add-to-cart")
	public ModelAndView addToCart(  @RequestParam(value = "id") Long id, 
									@RequestParam(value = "quantity") Long quantity,
									RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("redirect:/trang-chu/book-detail/" + id);
		try {
			orderUserService.addToCart(id,quantity);
			ra.addFlashAttribute("msg",env.getProperty("message.add.to.cart.success"));
		} catch (OverQuantityException e) {
			ra.addFlashAttribute("error",e.getMessage());
		} catch (Exception e) {
			
		}
		return mav;
	}
	
	/* Nh???n v??o ID book ????? thay ?????i s??? l?????ng s??ch c?? trong gi??? h??ng */
	@PostMapping(value = "/change-amount-in-cart", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String changeAmountInCart(	@RequestParam("increase") boolean increase,
										@RequestParam("id") Long id) {
		String message = "";
		try {
			orderUserService.changeAmountInOrderDetail(increase, id);
		} catch (ObjectNotFoundException e) {
			message = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/* L???y ID order detail ????? x??a order detail trong gi??? h??ng hi???n t???i */
	@PostMapping(value = "/delete-order-detail-incart")
	@ResponseBody
	public void deleteOrderDetail(@RequestParam("id") Long id) {
		try {
			orderUserService.deleteProductInCart(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Hi???n th??? th??ng tin gi??? h??ng hi???n t???i c???a user */
	@GetMapping(value = "/shopping-cart")
	public ModelAndView showShoppingCart() {
		ModelAndView mav = new ModelAndView("view/user/shopping-cart");
		User user = userService.getCurrentLoggedInUser();
		Order order = orderUserService.findCurrentOrder(user);
		if (order == null || order.getOrderDetails().isEmpty()) {
			mav.addObject("order", new ArrayList<>());
			return mav;
		}
		
		Collection<DisplayBookDto> bookDtos=order.getOrderDetails()
										.stream()
										.map(x->mapperBook.fromBookToDisplayBookDto(x.getBook()))
										.collect(Collectors.toList());
		mav.addObject("order", bookDtos);
		mav.addObject("username", user.getUsername());
		return mav;
	}
	
	/* Hi???n th??? trang check-out ????? th???c hi???n mua */
	@GetMapping(value = "/check-out")
	public ModelAndView showFormCheckOut() {
		ModelAndView mav = new ModelAndView("view/user/check-out");
		User user = userService.getCurrentLoggedInUser();
		Order order = orderUserService.findCurrentOrder(user);
		if (order == null || order.getOrderDetails().isEmpty()) {
			order = new Order();
			mav.addObject("order", new ArrayList<>());
		} 
		
		Collection<DisplayBookDto> bookDtos = order.getOrderDetails()
				.stream()
				.map(o->mapperBook.fromBookToDisplayBookDto(o.getBook()))
				.collect(Collectors.toList());
		
		Double totalMoney = 0d;
		for (DisplayBookDto x : bookDtos) {
			totalMoney += Double.parseDouble(x.getPrice()) * x.getAmountInCart() * 1000;
		}
		ProfileUserDto profileUserDto = profileService.getProfileDto();
		mav.addObject("profile",profileUserDto);
		mav.addObject("order", bookDtos);
		mav.addObject("totalMoney", numberFormat.format(totalMoney));
		mav.addObject("username", profileUserDto.getUsername());
		return mav;
	} 
	
	/* Th???c hi???n submit ????? mua order hi???n t???i */
	@PostMapping(value = "/complete-payment")
	public ModelAndView completePayment(@ModelAttribute ProfileUserDto profileUserDto,
			RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("redirect:/trang-chu");
		try {
			Message message = orderUserService.submitCurrentOrder(profileUserDto);
			ra.addFlashAttribute("message",message.getContent());
		} catch (OrderFailureException o) {
			ra.addFlashAttribute("error",o.getMessage());
		} catch (Exception e) {
			e.printStackTrace();		}
		return mav;
	}
	
	/* Tr??? v??? chu???i HTML order hi???n t???i khi hover v??o cart icon s??? hi???n th??? order hi???n t???i c???a ng?????i d??ng */
	@GetMapping(value = "/hover-cart-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String getPopUpCart() {
		User user = userService.getCurrentLoggedInUser();
		Order order = orderUserService.findCurrentOrder(user);
		if (order == null)
			return "";

		Collection<OrderDetail> orderDetails = order.getOrderDetails();
		StringBuilder builder = new StringBuilder();
		for (OrderDetail orderDetail : orderDetails) {
			Book book=orderDetail.getBook();
		String html = 
				  " 			  <li>" +
	              "                  <a href='"+domain+"/trang-chu/book-detail/"+book.getId()+"'/><img" +
	              "                      src='"+book.getImgLink()+"'" +
	              "                      alt='"+book.getBookTitle()+"'" +
	              "                      width='30'" +
	              "                      height='27'" +
	              "                  </a>" +
	              "                  <span class='cart-content-count'>x "+orderDetail.getAmount()+"</span>" +
	              "                  <strong><a href='"+domain+"/trang-chu/book-detail/"+book.getId()+"'>"+book.getBookTitle()+"</a></strong>" +
	              "                  <p style='color:red; font-weight:bold'>"+numberFormat.format(book.getPrice())+" ??</p>" +
	              "                </li>";
			builder.append(html);
		}
		return builder.toString();
	}

	
}























