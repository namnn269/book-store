package com.nam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nam.entity.Order;
import com.nam.entity.User;
import com.nam.repository.IOrderRepository;
import com.nam.service.IOrderService;

@Service
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private IOrderRepository orderRepo;
	
	@Override
	public Page<Order> getPageble(int pageNo, int pageSize, String sortByDate, int status, int cancelBy) {
		Sort sort;

		if (sortByDate.equals("1")) {
			sort = Sort.by(Direction.DESC, "orderDate");
		} else if (sortByDate.equals("2")) {
			sort = Sort.by(Direction.ASC, "orderDate");
		} else {
			sort = Sort.by("id");
		}
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Order> orders;
		if (cancelBy == 0) {
			orders = orderRepo.findByStatus(status, pageable);
		} else {
			orders = orderRepo.findByStatusAndCancelBy(status, cancelBy, pageable);
		}
		return orders;
	}

	@Override
	public Order findOrderById(Long orderId) {
		return orderRepo.findById(orderId).orElse(null);
	}
	
	/* Lấy ra order hiện tại của user đang đăng nhập */
	@Override
	public Order findCurrentOrder(User user) {
		List<Order> orders = orderRepo.findByUserAndStatus(user, 0, 0);
		if (orders.isEmpty())
			return null;
		return orders.get(0);
	}
	
}
















