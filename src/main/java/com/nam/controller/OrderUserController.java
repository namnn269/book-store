package com.nam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.OrderDto;
import com.nam.dto.ProfileUserDto;
import com.nam.entity.Order;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.IOrderMapper;
import com.nam.service.IOrderService;
import com.nam.service.IOrderUserService;
import com.nam.service.IProfileService;

@Controller
@RequestMapping(value = "/orders")
public class OrderUserController {
	@Autowired
	private IProfileService profileService;
	@Autowired
	private	IOrderService orderService;
	@Autowired
	private IOrderUserService orderUserService;
	@Autowired
	private IOrderMapper orderMapper;

	/* Hiển thị lịch sử order */
	@GetMapping(value = "/my-orders")
	public ModelAndView showUserOrders() {
		ModelAndView mav = new ModelAndView("view/user/list-orders-user");
		ProfileUserDto profileUserDto = new ProfileUserDto();
		try {
			profileUserDto = profileService.getProfileDto();
			mav.addObject("profile", profileUserDto);
			mav.addObject("username", profileUserDto.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@ResponseBody
	@GetMapping(value = "/get-table-orders-user")
	public String [] showUserTableOrders(
			@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,
			@RequestParam(defaultValue = "6", name = "pageSize") int pageSize,
			@RequestParam (name = "sortBy") String sortBydate,
			@RequestParam(defaultValue = "0", name = "status") int status){
		List<OrderDto> orders=orderUserService.findCurrentOrdersUser(pageNo, pageSize, sortBydate, status);
		System.out.println(status);
		return new String[] { 	getUserOrderTable(orders),
									getPagination(pageNo, pageSize, sortBydate, status)};
	}

	/* Hiển thị chi tiết order*/
	@GetMapping(value = "/order-detail/{orderId}")
	public ModelAndView showOrderDetail(@PathVariable("orderId") String orderId) {
		ModelAndView mav = new ModelAndView("view/user/order-detail-user");
		Order order = null;
		ProfileUserDto profileUserDto = new ProfileUserDto();
		try {
			profileUserDto = profileService.getProfileDto();
			mav.addObject("profile", profileUserDto);
			mav.addObject("username", profileUserDto.getUsername());
			order = orderService.findOrderById(Long.parseLong(orderId));
		} catch (Exception e) {
		}
		if (order == null) {
			mav.addObject("order", null);
		} else {
			OrderDto orderDto= orderMapper.fromOrderToOrderDto(order);
			mav.addObject("order", orderDto);
		}
		return mav;
	}
	

	/* Nhận vào Order ID để hủy 1 order */
	@PostMapping(value = "/cancel-order-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String cancelOrder(@RequestParam("orderId") Long orderId) {
		String html;
		try {
			Message message = orderUserService.cancelOrder(orderId);
			html = "<div class='alert alert-warning'>" + message.getContent() + "</div>";
		} catch (ObjectNotFoundException o) {
			html = "<div class='alert alert-danger'>" + o.getMessage() + "</div>";
			return o.getMessage();
		}
		return html;
	}
	
	private String getUserOrderTable(List<OrderDto> orderDtos) {
		String html = "";
		int i = 1;
		for (OrderDto order : orderDtos) {
			String status = order.getStatus() == 1 ? "Chờ xác nhận" : (order.getStatus() == 2 ? "Đã xác nhận" : "Đã hủy");
			html += "<tr>"
					+ "                    <td>"+i+"</td>"
					+ "                    <td>"+order.getUsername()+"</td>"
					+ "                    <td>"+order.getReceiver()+"</td>"
					+ "                    <td>"+order.getPhoneNumber()+"</td>"
					+ "                    <td>"+order.getAddress()+"</td>"
					+ "                    <td>"+order.getOrderDate()+"</td>"
					+ "                    <td>"+status+"</td>"
					+ "                    <td>"
					+ "                      <a href='/orders/order-detail/"+order.getId()+"'>"
					+ "                        <i class='bi bi-eye'></i>"
					+ "                      </a>"
					+ "                    </td>"
					+ "                  </tr>";	
			i++;
		}
		return html;
	}
	
	/* Trả về HTML chứa thông tin pagination user */
	private  String getPagination(int pageNo, int pageSize, String sortBydate, int status) {
		Page<Order> orderPage = orderUserService.getPageble(pageNo, pageSize, sortBydate, status);
		String html =			"<div class='hint-text'>"
				+ "                Tổng"
				+ "                <b>"+orderPage.getTotalElements()+"</b> đơn hàng"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#main' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= orderPage.getTotalPages(); i++) {
			html += "                  <li class='page-item " + (i == pageNo + 1 ? "active":" ")+"'>"
				+ "                    <a href='#main' class='page-link'>"+i+"</a>"
				+ "                  </li>";
		}
				
				html+=
				 "                <li class='next-page-item'>"
				+ "                  <a href='#main' class='page-link'>Next</a>"
				+ "                </li>"
				+ "              </ul>";
		
		return html;
	}
	
}
