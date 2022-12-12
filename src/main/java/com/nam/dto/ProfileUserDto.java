package com.nam.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUserDto {
	private String fullName;

	private String username;

	private String email;
	
	private String address;
	
	private String phoneNumber;
	
	private Date dateOfBirth;
	
	private String gender;
}
