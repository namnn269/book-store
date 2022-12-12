package com.nam.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nam.dto.UserDto;
import com.nam.entity.Role;
import com.nam.entity.User;

@Component
public class MapperUser {
	public UserDto fromUserToUserDto(User user) {
		UserDto userDto = new UserDto();
		
		List<String> role = user.getRoles().stream()
				.map(Role::getRoleName).collect(Collectors.toList());

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setEnabled(user.isEnabled());
		userDto.setFullName(user.getFullName());
		userDto.setRole(role);
		userDto.setUsername(user.getUsername());
		return userDto;
	}
}
