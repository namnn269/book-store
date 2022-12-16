package com.nam.mapper;

import com.nam.dto.UserDto;
import com.nam.entity.User;

public interface IUserMapper {

	UserDto fromUserToUserDto(User user);

}
