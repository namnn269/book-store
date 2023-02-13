package com.nam.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nam.dto.AdminOrderDto;
import com.nam.entity.Order;
import com.nam.exception_mesage.Message;

public interface IOrderAdminService {

	Message confirmOrder(Long orderId);

	Message cancelOrder(Long orderId);
	
	List<AdminOrderDto> getListCreatedOrder(int pageNo, int pageSize, String sortByDate);

	List<AdminOrderDto> getListConfirmedOrders(int pageNo, int pageSize, String sortByDate);

	List<AdminOrderDto> getListCanceledOrders(int pageNo, int pageSize, String sortBydate, int cancelBy);

	Page<Order> getPageble(int pageNo, int pageSize, String sortByDate, int status, int cancelBy);

	Order findOrderById(Long orderId);

}
