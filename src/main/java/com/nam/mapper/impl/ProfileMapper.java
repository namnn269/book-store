package com.nam.mapper.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nam.dto.ProfileUserDto;
import com.nam.entity.ProfileUser;
import com.nam.entity.User;
import com.nam.mapper.IProfileMapper;
import com.nam.repository.IProfileRepository;

@Component
public class ProfileMapper implements IProfileMapper {
	@Autowired
	private IProfileRepository profileRepo;

	@Override
	public ProfileUserDto fromUserToProfileDto(User user) {
		ProfileUserDto profileUserDto = new ProfileUserDto();
		if (user == null)
			return null;
		List<ProfileUser> opProfile = profileRepo.findByUser(user);
		ProfileUser profileUser;
		if (opProfile.isEmpty())
			profileUser = new ProfileUser();
		else
			profileUser = opProfile.get(0);
		profileUserDto.setUsername(user.getUsername());
		profileUserDto.setEmail(user.getEmail());
		profileUserDto.setFullName(user.getFullName());
		if (profileUser != null) {
			profileUserDto.setGender(profileUser.getGender());
			profileUserDto.setAddress(profileUser.getAddress());
			profileUserDto.setDateOfBirth(profileUser.getDateOfBirth());
			profileUserDto.setPhoneNumber(profileUser.getPhoneNumber());
		}

		return profileUserDto;
	}
}






