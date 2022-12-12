package com.nam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
	private Long id;

	private String fullName;
	
	private String username;

	private boolean enabled;

	private List<Long> role_id;
}
