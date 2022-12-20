package com.nam.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nam.dto.UserRegistrationFormDto;

public class FormValidation {
	public static String checkFormSignup(UserRegistrationFormDto userDto) {
		if (userDto.getEmail() == null || userDto.getEmail().equals(""))
			return "Email không được để trống!";
		if (userDto.getUsername() == null || userDto.getUsername().equals(""))
			return "Username không được để trống!";
		if (userDto.getFullName() == null || userDto.getFullName().equals(""))
			return "Fullname không được để trống!";
		if (userDto.getPassword() == null || userDto.getRePassword() == null || userDto.getPassword().equals("")
				|| userDto.getRePassword().equals(""))
			return "Mật khẩu không được để trống!";
		if (!userDto.getPassword().equals(userDto.getRePassword()))
			return "Mật khẩu không trùng khớp";
		String checkPassword = checkPassword(userDto.getPassword());
		if (checkPassword != null) {
			System.out.println("check pass : "+checkPassword);
			return checkPassword;
		}
		String checkEmail = checkEmail(userDto.getEmail());
		if (checkEmail != null)
			return checkEmail;
		return null; 
	}

	public static String checkPassword(String password) {
		String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		boolean matchFound=matcher.find();
		if (!matchFound) {
			return "Mật khẩu tối thiếu 8 ký tự, chứa ký tự hoa, thường và đặc biệt";
		}
		return null;
	}
	
	public static String checkEmail(String email) {
		String	regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{1,6}$";
		Pattern	pattern = Pattern.compile(regex);
		Matcher	matcher = pattern.matcher(email);
		boolean matchFound=matcher.find();
		if (!matchFound)
			return "Định dạng email không hợp lệ!";
		return null;
	}
	
}
