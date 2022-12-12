package com.nam.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.nam.exception_mesage.OrderFailureException;
import com.nam.exception_mesage.OverQuantityException;
import com.nam.mapper.MapperBook;
import com.nam.service.IBookService;
import com.nam.service.IOrderService;
import com.nam.service.IProfileService;
import com.nam.service.IUserService;

@Controller
@RequestMapping(value = "/cart")
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IBookService bookService;
	
	@Autowired
	private IProfileService profileService;
	
	@Autowired
	private MapperBook mapperBook;
	
	@PostMapping(value = "/add-to-cart")
	public ModelAndView addToCart(  @RequestParam(value = "id") Long id, 
							@RequestParam(value = "quantity") Long quantity) {
		ModelAndView mav=new ModelAndView("view/user/book-detail");
		DisplayBookDto bookDto = bookService.findBookDetailById(id);
		mav.addObject("book", bookDto);
		try {
			orderService.addToCart(id,quantity);
			mav.addObject("message","Thêm sản phẩm thành công!");
			mav.addObject("username",userService.getCurrentLoggedInUser().getUsername());
		} catch (OverQuantityException e) {
			mav.addObject("username",userService.getCurrentLoggedInUser().getUsername());
			mav.addObject("error","Số lượng sản phẩm vượt quá số lượng hiện có!");
		} catch (Exception e) {
			
		}
		return mav;
	}
	
	
	@PostMapping(value = "/change-amount-in-cart")
	@ResponseBody
	public void changeAmountInCart(@RequestParam("increase") boolean increase,
									@RequestParam("id") Long id) {
		try {
			orderService.changeAmountInOrderDetail(increase,id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@PostMapping(value = "/delete-order-detail-incart")
	@ResponseBody
	public void deleteOrderDetail(@RequestParam("id") Long id) {
		try {
			orderService.deleteProductInCart(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "/shopping-cart")
	public ModelAndView showShoppingCart() {
		ModelAndView mav = new ModelAndView("view/user/shopping-cart");
		User user = userService.getCurrentLoggedInUser();
		Order order = orderService.findCurrentOrder(user);
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
	
	@GetMapping(value = "/check-out")
	public ModelAndView showFormCheckOut() {
		ModelAndView mav = new ModelAndView("view/user/check-out");
		User user = userService.getCurrentLoggedInUser();
		Order order = orderService.findCurrentOrder(user);
		if (order == null || order.getOrderDetails().isEmpty()) {
			order=new Order();
			mav.addObject("order", new ArrayList<>());
		}
		
		Collection<DisplayBookDto> bookDtos=order.getOrderDetails()
				.stream()
				.map(x->mapperBook.fromBookToDisplayBookDto(x.getBook()))
				.collect(Collectors.toList());
		Double totalMoney = 0d;
		for (DisplayBookDto x : bookDtos) {
			totalMoney += x.getPrice() * x.getAmountInCart();
		}
		ProfileUserDto profileUserDto = profileService.getProfileDto();
		mav.addObject("profile",profileUserDto);
		mav.addObject("order", bookDtos);
		mav.addObject("totalMoney", totalMoney);
		mav.addObject("username", profileUserDto.getUsername());
		return mav;
	} 
	
	@GetMapping(value = "/complete-payment")
	public ModelAndView completePayment(RedirectAttributes ra) {
		ModelAndView mav = new ModelAndView("redirect:/trang-chu");
		try {
			Message message = orderService.completeCurrentOrder();
			ra.addFlashAttribute("message",message.getContent());
		} catch (OrderFailureException o) {
			ra.addFlashAttribute("error",o.getMessage());
		} catch (Exception e) {
			e.printStackTrace();		}
		return mav;
	}
	

	@GetMapping(value = "/hover-cart-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String getPopUpCart() {
		User user=userService.getCurrentLoggedInUser();
		Order order=orderService.findCurrentOrder(user);
		if(order==null)return "";
		
		Collection<OrderDetail> orderDetails=order.getOrderDetails();
		StringBuilder builder=new StringBuilder();
		for (OrderDetail orderDetail : orderDetails) {
			Book book=orderDetail.getBook();
		String html = 
				  " 			  <li>" +
	              "                  <a href='/trang-chu/book-detail/"+book.getId()+"'/><img" +
	              "                      src='"+book.getImgLink()+"'" +
	              "                      alt='"+book.getBookTitle()+"'" +
	              "                      width='30'" +
	              "                      height='27'" +
	              "                  </a>" +
	              "                  <span class='cart-content-count'>x "+orderDetail.getAmount()+"</span>" +
	              "                  <strong><a href='trang-chu/book-detail/"+book.getId()+"'>"+book.getBookTitle()+"</a></strong>" +
	              "                  <em>$"+book.getPrice()*orderDetail.getAmount()+"</em>" +
	              "                </li>";
			builder.append(html);
		}
		return builder.toString();
	}

	
}























