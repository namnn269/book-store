package com.nam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.nam.dto.ProfileUserDto;
import com.nam.entity.ProfileUser;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ValidFormException;
import com.nam.mapper.IProfileMapper;
import com.nam.repository.IProfileRepository;
import com.nam.service.IProfileService;
import com.nam.service.IUserService;

@Service
@PropertySource(value = "messages.properties", encoding = "utf-8")
public class ProfileServiceImpl implements IProfileService {

	@Autowired
	IUserService userService;
	@Autowired
	IProfileMapper profileMapper;
	@Autowired
	private IProfileRepository profileRepo;
	@Autowired
	private Environment env;

	/* Thực hiện lưu profile của người dùng */
	@Override
	public Message save(ProfileUser profileUser) {
		User user = userService.getCurrentLoggedInUser();
		try {
			Long.parseLong(profileUser.getPhoneNumber());
			int phoneNumberLength = profileUser.getPhoneNumber().length();
			if (phoneNumberLength < 9 || phoneNumberLength > 11)
				throw new ValidFormException(env.getProperty("message.invalid.phone"));
		} catch (Exception e) {
			throw new ValidFormException(env.getProperty("message.invalid.phone"));
		}
		profileUser.setUser(user);
		profileRepo.save(profileUser);
		return new Message(env.getProperty("message.update.info.success"));
	}

	/* Lấy ra profile của người dùng đang đăng nhập */
	@Override
	public ProfileUser getProfile() {
		User user = userService.getCurrentLoggedInUser();
		List<ProfileUser> opProfile = profileRepo.findByUser(user);
		ProfileUser profile;
		if (opProfile.isEmpty())
			profile = new ProfileUser();
		else
			profile = opProfile.get(0);
		return profile;
	}

	/* Lấy ra profile DTO của người dùng đang đăng nhập */
	@Override
	public ProfileUserDto getProfileDto() {
		User user = userService.getCurrentLoggedInUser();
		ProfileUserDto profileUserDto = profileMapper.fromUserToProfileDto(user);
		return profileUserDto;
	}

}
