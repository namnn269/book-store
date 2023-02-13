package com.nam.mapper;

import com.nam.dto.AdminOrderDto;
import com.nam.dto.OrderDto;
import com.nam.entity.Order;

public interface IOrderMapper {
	
	OrderDto fromOrderToOrderDto(Order order);
	AdminOrderDto mapFromOrderToAdminOrderDto (Order order);
}
