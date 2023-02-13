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
	
	private String totalMoney;
	
	private UserDto buyer;
	
	private String receiver;
	
	private String phoneNumber;
	
	private String address;
	
	private String date;
	
	private int status;
	
	private int cancelBy;
	
	private Collection<OrderDetailDto> orderDetailDtos;
}
