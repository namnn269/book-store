package com.nam.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDto {
	private Long id;
	
	private UserDto buyer;
	
	private Collection<PurchasedOrderDetailDto> bookDtos;
}
