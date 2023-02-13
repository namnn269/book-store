package com.nam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nam.dto.AdminOrderDto;
import com.nam.dto.OrderDto;
import com.nam.entity.Order;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.IOrderMapper;
import com.nam.service.IOrderAdminService;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {
	@Autowired
	private	IOrderAdminService orderAdminService;
	@Autowired
	private IOrderMapper orderMapper;
	
	/* Hiển thị trang quản lý các order chưa được xác thực admin */
	@GetMapping(value = "/management-order")
	public ModelAndView managementOrder() {
		ModelAndView mav = new ModelAndView("view/admin/management-order");
		try {
			mav.addObject("message",null);
			mav.addObject("error", null);
		} catch (Exception e) {
			mav.addObject("createdOrders",new ArrayList<>());
			e.printStackTrace();
		}
		return mav;
	}
	
	/* Hiển thị trang quản lý các order đã được xác thực admin */
	@GetMapping(value = "/confirmed-orders")
	public ModelAndView showConfirmedOrder() {
		ModelAndView mav = new ModelAndView("view/admin/confirmed-orders");
		try {
			mav.addObject("message",null);
			mav.addObject("error", null);
		} catch (Exception e) {
			mav.addObject("createdOrders",new ArrayList<>());
			e.printStackTrace();
		}
		return mav;
	}
	
	/* Hiển thị trang quản lý các order đã được xác thực admin */
	@GetMapping(value = "/canceled-orders")
	public ModelAndView showCanceledOrder() {
		ModelAndView mav = new ModelAndView("view/admin/canceled-orders");
		try {
			mav.addObject("message",null);
			mav.addObject("error", null);
		} catch (Exception e) {
			mav.addObject("createdOrders",new ArrayList<>());
			e.printStackTrace();
		}
		return mav;
	}

	/* Hiển thị chi tiết order*/
	@GetMapping(value = "/order-detail/{orderId}")
	public ModelAndView showOrderDetail(@PathVariable("orderId") String orderId) {
		ModelAndView mav = new ModelAndView("view/admin/order-detail-admin");
		Order order = null;
		try {
			order = orderAdminService.findOrderById(Long.parseLong(orderId));
		} catch (Exception e) {
		}
		if (order == null) {
			mav.addObject("order", null);
		} else {
			OrderDto orderDto = orderMapper.fromOrderToOrderDto(order);
			mav.addObject("order", orderDto);
		}
		return mav;
	}
	
	/* Nhận vào Order ID để thực hiển xác nhận 1 order */
	@PostMapping(value = "/confirm-order-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public ResponseEntity<?> confirmOrder(@RequestParam("orderId") Long orderId) {
		String html;
		try {
			Message message = orderAdminService.confirmOrder(orderId);
			 html = "<div class='alert alert-success'>" + message.getContent() + "</div>";
		} catch (ObjectNotFoundException o) {
			 html = "<div class='alert alert-danger'>" + o.getMessage() + "</div>";
			 return new ResponseEntity<>(html, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(html, HttpStatus.OK);
	}

	/* Nhận vào Order ID để hủy 1 order */
	@PostMapping(value = "/cancel-order-ajax", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String cancelOrder(@RequestParam("orderId") Long orderId) {
		String html;
		try {
			Message message = orderAdminService.cancelOrder(orderId);
			html = "<div class='alert alert-warning'>" + message.getContent() + "</div>";
		} catch (ObjectNotFoundException o) {
			html = "<div class='alert alert-danger'>" + o.getMessage() + "</div>";
			return o.getMessage();
		}
		return html;
	}
	
	/* Hiển thị bảng các order chưa được xác thực admin AJAX*/
	@GetMapping(value = "/management-order-ajax")
	@ResponseBody
	public String[] managementOrderAjax(
			@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,
			@RequestParam(defaultValue = "6", name = "pageSize") int pageSize,
			@RequestParam (name="sortBy") String sortBydate) {
		
		List<AdminOrderDto> adminOrderDtos = orderAdminService.getListCreatedOrder(pageNo, pageSize, sortBydate);

		return new String[] {	getSubmitedOrderTable(adminOrderDtos),
								getWaitingConfirmationOrdersPagination(pageNo, pageSize, sortBydate, 0) };
	}
	
	/* Hiển thị bảng các order đã được xác thực admin AJAX*/
	@GetMapping(value = "/management-confirmed-order-ajax")
	@ResponseBody
	public String[] managementConfirmedOrderAjax(
			@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,
			@RequestParam(defaultValue = "6", name = "pageSize") int pageSize,
			@RequestParam (name="sortBy") String sortBydate) {
		
		List<AdminOrderDto> adminOrderDtos = orderAdminService.getListConfirmedOrders(pageNo, pageSize, sortBydate);
		
		return new String[] {	getConfirmedOrderTable(adminOrderDtos),
								getConfirmedOrdersPagination(pageNo, pageSize, sortBydate, 0) };
	}
	
	/* Hiển thị bảng các order đã được xác thực admin AJAX*/
	@GetMapping(value = "/management-canceled-order-ajax")
	@ResponseBody
	public String[] managementCanceledOrderAjax(
			@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,
			@RequestParam(defaultValue = "6", name = "pageSize") int pageSize,
			@RequestParam (name = "sortBy") String sortBydate,
			@RequestParam(defaultValue = "0",name = "cancelBy") int cancelBy) {
		List<AdminOrderDto> adminOrderDtos = orderAdminService.getListCanceledOrders(pageNo, pageSize, sortBydate, cancelBy);
		
		return new String[] {	getCanceledOrderTable(adminOrderDtos),
								getCanceledOrdersPagination(pageNo, pageSize, sortBydate, cancelBy) };
	}
	
	private String getSubmitedOrderTable(List<AdminOrderDto> adminOrderDtos) {
		String html = "";
		int i = 1;
		for (AdminOrderDto order : adminOrderDtos) {
		html += "<tr>"
				+ "                    <td>"+i+"</td>"
				+ "                    <td>"+order.getBuyer().getUsername()+"</td>"
				+ "                    <td>"+order.getReceiver()+"</td>"
				+ "                    <td>"+order.getPhoneNumber()+"</td>"
				+ "                    <td>"+order.getAddress()+"</td>"
				+ "                    <td>"+order.getDate()+"</td>"
				+ "                    <td>"
				+ "                      <button"
				+ "                        class='confirm-btn action-button'"
				+ "                        type='button'"
				+ "                        value='"+order.getId()+"'"
				+ "                      >"
				+ "                        <i class='bi bi-check-circle-fill icon-confirm'></i>"
				+ "                      </button>"
				+ "                    </td>"
				+ "                    <td>"
				+ "                      <button"
				+ "                        class='cancel-btn action-button'"
				+ "                        type='button'"
				+ "                        value='"+order.getId()+"'"
				+ "                      >"
				+ "                        <i class='bi bi-x-circle-fill icon-cancel'></i>"
				+ "                      </button>"
				+ "                    </td>"
				+ "                    <td>"
				+ "                      <a href='/admin/order-detail/"+order.getId()+"'>"
				+ "                        <i class='bi bi-eye'></i>"
				+ "                      </a>"
				+ "                    </td>"
				+ "                  </tr>";	
		i++;
		}
		return html;
	}
	
	private String getConfirmedOrderTable(List<AdminOrderDto> adminOrderDtos) {
		String html = "";
		int i = 1;
		for (AdminOrderDto order : adminOrderDtos) {
			html += "<tr>"
					+ "                    <td>"+i+"</td>"
					+ "                    <td>"+order.getBuyer().getUsername()+"</td>"
					+ "                    <td>"+order.getReceiver()+"</td>"
					+ "                    <td>"+order.getPhoneNumber()+"</td>"
					+ "                    <td>"+order.getAddress()+"</td>"
					+ "                    <td>"+order.getDate()+"</td>"
					+ "                    <td>"
					+ "                      <a href='/admin/order-detail/"+order.getId()+"'>"
					+ "                        <i class='bi bi-eye'></i>"
					+ "                      </a>"
					+ "                    </td>"
					+ "                  </tr>";	
			i++;
		}
		return html;
	}
	
	private String getCanceledOrderTable(List<AdminOrderDto> adminOrderDtos) {
		String html = "";
		int i = 1;
		for (AdminOrderDto order : adminOrderDtos) {
			html += "<tr>"
					+ "                    <td>"+i+"</td>"
					+ "                    <td>"+order.getBuyer().getUsername()+"</td>"
					+ "                    <td>"+order.getReceiver()+"</td>"
					+ "                    <td>"+order.getPhoneNumber()+"</td>"
					+ "                    <td>"+order.getAddress()+"</td>"
					+ "                    <td>"+order.getDate()+"</td>"
					+ "                    <td>"+(order.getCancelBy()==1?"Admin":"User")+"</td>"
					+ "                    <td>"
					+ "                      <a href='/admin/order-detail/"+order.getId()+"'>"
					+ "                        <i class='bi bi-eye'></i>"
					+ "                      </a>"
					+ "                    </td>"
					+ "                  </tr>";	
			i++;
		}
		return html;
	}
	
	private String getWaitingConfirmationOrdersPagination(int pageNo, int pageSize, String sortBydate, int cancelBy) {
		return getPagination(pageNo, pageSize, sortBydate, 1, 0);
	}
	
	private String getConfirmedOrdersPagination(int pageNo, int pageSize, String sortBydate, int cancelBy) {
		return getPagination(pageNo, pageSize, sortBydate, 2, 0);
	}

	private String getCanceledOrdersPagination(int pageNo, int pageSize, String sortBydate, int cancelBy) {
		return getPagination(pageNo, pageSize, sortBydate, 3, cancelBy);
	}

	/* Trả về HTML chứa thông tin pagination user */
	private  String getPagination(int pageNo, int pageSize, String sortBydate, int status, int cancelBy) {
		Page<Order> orderPage = orderAdminService.getPageble(pageNo, pageSize, sortBydate, status, cancelBy);
		String html =			"<div class='hint-text'>"
				+ "                Tổng"
				+ "                <b>"+orderPage.getTotalElements()+"</b> đơn hàng"
				+ "              </div>"
				+ "              <ul class='pagination'>"
				+ "                <li class='previous-page-item'>"
				+ "                  <a href='#' class='page-link'>Previous</a>"
				+ "                </li>";
				
		for (int i = 1; i <= orderPage.getTotalPages(); i++) {
			html += "                  <li class='page-item " + (i == pageNo + 1 ? "active":" ")+"'>"
				+ "                    <a href='#' class='page-link'>"+i+"</a>"
				+ "                  </li>";
		}
				
				html+=
				 "                <li class='next-page-item'>"
				+ "                  <a href='#' class='page-link'>Next</a>"
				+ "                </li>"
				+ "              </ul>";
		
		return html;
	}
	
}


































