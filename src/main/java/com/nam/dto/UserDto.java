package com.nam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;

	private String fullName;

	private String username;

	private String email;

	private boolean enabled = false;

	private List<String> role;

//	private ProfileUser profileUser;

//	private Collection<Order> orders = new ArrayList<>();

//	private Collection<Discount> discounts = new ArrayList<>();
}
