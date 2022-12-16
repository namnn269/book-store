package com.nam.mapper;

import com.nam.dto.ProfileUserDto;
import com.nam.entity.User;

public interface IProfileMapper {

	ProfileUserDto fromUserToProfileDto(User user);

}
