package com.nam.mapper;

import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.OrderDetail;

public interface IOrderDetailMapper {

	PurchasedOrderDetailDto fromOrderDetailToPurchasedBookDto(OrderDetail orderDetail);

}
