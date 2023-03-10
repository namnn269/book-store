package com.nam.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import com.nam.dto.UserDto;
import com.nam.dto.UserRegistrationFormDto;
import com.nam.dto.UserUpdateDto;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;

public interface IUserService {
	List<User> findAll();

	List<UserDto> findAll(int pageNo, int pageSize, String searchKey, Long roleId, int status);

	Page<User> getPageable(int pageNo, int pageSize, String searchKey, Long roleId, int status);

	Optional<?> saveNewRegisterUser(UserRegistrationFormDto userRegDto);

	Message delete(Long id);

	Optional<User> findById(Long id);

	Message update(UserUpdateDto dto);

	UserUpdateDto getUpdateUser(Long id);
	
	User getCurrentLoggedInUser();

	Message changePassword(String oldPassword, String newPassword1, String newPassword2);

	Message sendEmailToConfirmBeforeResetPassowrd(String username, String email, HttpServletRequest http);

	Message resetPassword(User user);

}
