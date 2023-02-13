package com.nam.mapper;

import com.nam.dto.OrderDetailDto;
import com.nam.entity.OrderDetail;

public interface IOrderDetailMapper {

	OrderDetailDto fromOrderDetailToOrderDetailDto(OrderDetail orderDetail);

}
