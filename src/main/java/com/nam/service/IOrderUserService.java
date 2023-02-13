package com.nam.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nam.dto.OrderDto;
import com.nam.dto.ProfileUserDto;
import com.nam.entity.Order;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;

public interface IOrderUserService {
	void addToCart(Long id, Long quantity);

	Order findCurrentOrder(User user);

	void changeAmountInOrderDetail(boolean increase, Long id);

	void deleteProductInCart(Long id);

	Message submitCurrentOrder(ProfileUserDto profileUserDto);

	List<OrderDto> findCurrentOrdersUser(int pageNo, int pageSize, String sortBydate, int status);

	Page<Order> getPageble(int pageNo, int pageSize, String sortBydate, int status);

	Message cancelOrder(Long orderId);

}
