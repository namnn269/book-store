package com.nam.service;

import org.springframework.data.domain.Page;

import com.nam.entity.Order;
import com.nam.entity.User;

public interface IOrderService {

	Page<Order> getPageble(int pageNo, int pageSize, String sortByDate, int status, int cancelBy);

	Order findOrderById(Long orderId);

	Order findCurrentOrder(User user);

}
