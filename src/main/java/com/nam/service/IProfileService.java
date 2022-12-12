package com.nam.service;

import com.nam.dto.ProfileUserDto;
import com.nam.entity.ProfileUser;
import com.nam.exception_mesage.Message;

public interface IProfileService {
	Message save(ProfileUser profileUser);

	ProfileUser getProfile();

	ProfileUserDto getProfileDto();
}
