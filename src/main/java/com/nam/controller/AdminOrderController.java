package com.nam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.AdminOrderDto;
import com.nam.dto.ProfileUserDto;
import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.service.IOrderDetailService;
import com.nam.service.IOrderService;
import com.nam.service.IProfileService;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {
	@Autowired
	IProfileService profileService;
	@Autowired
	IOrderDetailService orderDetailService;
	@Autowired
	IOrderService orderService;
	
	@GetMapping(value = "/management-order")
	public ModelAndView managementOrder() {
		ModelAndView mav = new ModelAndView("view/admin/management-order");
		try {
		List<AdminOrderDto> orderDtos= orderService.getListCreatedOrder();
			mav.addObject("createdOrders",orderDtos);
			mav.addObject("message",null);
			mav.addObject("error", null);
		} catch (Exception e) {
			mav.addObject("createdOrders",new ArrayList<>());
			e.printStackTrace();
		}
		return mav;
	}
	
	@GetMapping(value = "/confirmed-orders")
	public ModelAndView showConfirmedOrder() {
		ModelAndView mav = new ModelAndView("view/admin/confirmed-orders");
		try {
			List<AdminOrderDto> orderDtos= orderService.getListConfirmedOrders();
			mav.addObject("confirmedOrders",orderDtos);
			mav.addObject("message",null);
			mav.addObject("error", null);
		} catch (Exception e) {
			mav.addObject("createdOrders",new ArrayList<>());
			e.printStackTrace();
		}
		return mav;
	}
	
	@PostMapping(value = "/management-order-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String confirmOrder(@RequestParam("orderId") Long orderId) {
		String html;
		try {
			Message message = orderService.confirmOrder(orderId);
			 html = "<div class='alert alert-success'>" + message.getContent() + "</div>";
		} catch (ObjectNotFoundException o) {
			 html = "<div class='alert alert-danger'>" + o.getMessage() + "</div>";
			return o.getMessage();
		}
		return html;
	}
}














