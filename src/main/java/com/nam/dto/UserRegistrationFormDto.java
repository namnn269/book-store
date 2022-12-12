package com.nam.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationFormDto {

	@NotNull
	private String fullName;

	@NotNull
	private String username;

	@NotNull
	@Min(2)
	private String password;
	
	@NotNull
	@Min(2)
	private String rePassword;
	
	@NotNull
	private String email;

}
