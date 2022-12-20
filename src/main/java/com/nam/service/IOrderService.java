package com.nam.service;

import java.util.List;

import com.nam.dto.AdminOrderDto;
import com.nam.entity.Order;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;

public interface IOrderService {
	void addToCart(Long id, Long quantity);

	Order findCurrentOrder(User user);

	void changeAmountInOrderDetail(boolean increase, Long id);

	void deleteProductInCart(Long id);

	Message completeCurrentOrder();

	Message confirmOrder(Long orderId);

	List<AdminOrderDto> getListCreatedOrder();

	List<AdminOrderDto> getListConfirmedOrders();


}
