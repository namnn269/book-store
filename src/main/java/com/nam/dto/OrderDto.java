package com.nam.dto;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
	private Long id;

	private String totalMoney;

	private String receiver;

	private String username;
	
	private String phoneNumber;
	
	private String address;
	
	private String orderDate;
	
	private int status;
	
	@Default
	private int cancelBy = 0;
	@Default
	private Collection<OrderDetailDto> orderDetails = new ArrayList<>();

}
