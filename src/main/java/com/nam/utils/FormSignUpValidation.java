package com.nam.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nam.dto.UserRegistrationFormDto;

public class FormSignUpValidation {
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
		String checkEmailPass = checkEmailAndPassword(userDto);
		if (checkEmailPass != null)
			return checkEmailPass;
		return null;
	}

	private static String checkEmailAndPassword(UserRegistrationFormDto userDto) {
		String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(userDto.getPassword());
		while (!matcher.find())
			return "Mật khẩu tối thiếu 8 ký tự, chứa ký tự hoa, thường và đặc biệt";

		regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{1,6}$";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(userDto.getEmail());
		while (!matcher.find())
			return "Định dạng email không hợp lệ!";
		return null;
	}
}
