package com.nam.service;

import java.util.List;

import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.OrderDetail;
import com.nam.exception_mesage.Message;

public interface IOrderDetailService {
	Message save(OrderDetail orderDetail);

	List<PurchasedOrderDetailDto> getListPurchasedOrderDetailDtos();

}
