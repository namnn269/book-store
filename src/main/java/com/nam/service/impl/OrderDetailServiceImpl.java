package com.nam.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.Order;
import com.nam.entity.OrderDetail;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.mapper.IOrderDetailMapper;
import com.nam.repository.IOrderDetailRepository;
import com.nam.repository.IOrderRepository;
import com.nam.service.IOrderDetailService;
import com.nam.service.IUserService;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService {

	@Autowired
	private IOrderDetailRepository orderDetailRepo;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderRepository orderRepo;
	@Autowired
	private IOrderDetailMapper orderDetailMapper;

	/* Lưu order detail */
	@Override
	public Message save(OrderDetail orderDetail) {
		orderDetailRepo.save(orderDetail);
		return null;
	}

	/* Lấy ra danh sách order detail đã mua của khách */
	@Override
	public List<PurchasedOrderDetailDto> getListPurchasedOrderDetailDtos() {
		User user = userService.getCurrentLoggedInUser();

		List<Order> orders = orderRepo.findByUserAndStatus(user, 1, 2);
		List<PurchasedOrderDetailDto> purchasedOrderDetailDtos = new ArrayList<>();

		orders.stream()
		.forEach(order -> order
							.getOrderDetails()
							.stream()
							.forEach(orderDetail -> {purchasedOrderDetailDtos.add(orderDetailMapper.fromOrderDetailToPurchasedBookDto(orderDetail));}
				));
		return purchasedOrderDetailDtos;
	}


}






