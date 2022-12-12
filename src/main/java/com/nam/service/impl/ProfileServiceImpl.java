package com.nam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nam.dto.ProfileUserDto;
import com.nam.entity.ProfileUser;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ValidFormException;
import com.nam.mapper.MapperProfile;
import com.nam.repository.IProfileRepository;
import com.nam.service.IProfileService;
import com.nam.service.IUserService;

@Service
public class ProfileServiceImpl implements IProfileService {

	@Autowired
	IUserService userService;
	@Autowired
	MapperProfile mapperProfile;
	@Autowired
	private IProfileRepository profileRepo;

	// update or add profile
	@Override
	public Message save(ProfileUser profileUser) {
		User user = userService.getCurrentLoggedInUser();
		try {
			Long.parseLong(profileUser.getPhoneNumber());
			int phoneNumberLength = profileUser.getPhoneNumber().length();
			if (phoneNumberLength < 9 || phoneNumberLength > 11)
				throw new ValidFormException("SĐT không hợp lệ");
		} catch (Exception e) {
			throw new ValidFormException("SĐT không hợp lệ");
		}
		profileUser.setUser(user);
		profileRepo.save(profileUser);
		return new Message("Update successfully");
	}

	// get profile user
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

	// show info user
	@Override
	public ProfileUserDto getProfileDto() {
		User user = userService.getCurrentLoggedInUser();
		ProfileUserDto profileUserDto = mapperProfile.fromUserToProfileDto(user);
		return profileUserDto;
	}

}
