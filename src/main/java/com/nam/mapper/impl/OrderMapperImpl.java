package com.nam.mapper.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nam.dto.AdminOrderDto;
import com.nam.dto.OrderDetailDto;
import com.nam.dto.OrderDto;
import com.nam.entity.Order;
import com.nam.mapper.IOrderDetailMapper;
import com.nam.mapper.IOrderMapper;
import com.nam.mapper.IUserMapper;

@Component
public class OrderMapperImpl implements IOrderMapper {
	@Autowired
	private IOrderDetailMapper orderDetailMapper;
	@Autowired
	private IUserMapper userMapper;
	
	private NumberFormat numberFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
	private SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
	
	@Override
	public OrderDto fromOrderToOrderDto(Order order) {
		Collection<OrderDetailDto> bookDtos = order.getOrderDetails().stream()
				.map(o -> orderDetailMapper.fromOrderDetailToOrderDetailDto(o)).collect(Collectors.toList());
		Double totalMoney = 0d;
		for (OrderDetailDto x : bookDtos) {
			totalMoney += Double.parseDouble(x.getPrice()) * x.getQuantity() * 1000;
		}
		
		OrderDto orderDto=OrderDto.builder()
				.id(order.getId())
				.orderDate(dateFormat.format(order.getOrderDate()))
				.status(order.getStatus())
				.cancelBy(order.getCancelBy())
				.address(order.getAddress())
				.phoneNumber(order.getPhoneNumbber())
				.receiver(order.getReceiver())
				.username(order.getUser().getUsername())
				.totalMoney(numberFormat.format(totalMoney))
				.orderDetails(bookDtos)
				.build();
		return orderDto; 
	}
	
	@Override
	public AdminOrderDto mapFromOrderToAdminOrderDto (Order order) {
		Collection<OrderDetailDto> bookDtos = order.getOrderDetails().stream()
				.map(o -> orderDetailMapper.fromOrderDetailToOrderDetailDto(o)).collect(Collectors.toList());
		Double totalMoney = 0d;
		for (OrderDetailDto x : bookDtos) {
			totalMoney += Double.parseDouble(x.getPrice()) * x.getQuantity() * 1000;
		}
		
		AdminOrderDto adminOrder = new AdminOrderDto();
		adminOrder.setId(order.getId());
		adminOrder.setBuyer(userMapper.fromUserToUserDto(order.getUser()));
		adminOrder.setReceiver(order.getReceiver());
		adminOrder.setStatus(order.getStatus());
		adminOrder.setCancelBy(order.getCancelBy());
		adminOrder.setPhoneNumber(order.getPhoneNumbber());
		adminOrder.setAddress(order.getAddress());
		adminOrder.setDate(dateFormat.format(order.getOrderDate()));
		adminOrder.setOrderDetailDtos(bookDtos);
		adminOrder.setTotalMoney(numberFormat.format(totalMoney));
		return adminOrder;
	}

}
