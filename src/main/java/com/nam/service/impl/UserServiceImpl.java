package com.nam.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nam.dto.UserDto;
import com.nam.dto.UserRegistrationFormDto;
import com.nam.dto.UserUpdateDto;
import com.nam.entity.Role;
import com.nam.entity.User;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.ValidFormException;
import com.nam.mapper.MapperUser;
import com.nam.repository.IOrderRepository;
import com.nam.repository.IRoleRepository;
import com.nam.repository.IUserRepository;
import com.nam.security.UserDetailsImpl;
import com.nam.service.IUserService;
import com.nam.utils.FormSignUpValidation;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private IOrderRepository orderRepo;
	@Autowired 
	private MapperUser mapperUser;


	@Override
	public List<User> findAll() {
		List<User> list = userRepo.findAll();
		return list;
	}

	@Override
	public Optional<?> saveNewRegisterUser(UserRegistrationFormDto userRegDto) {
		String validForm = FormSignUpValidation.checkFormSignup(userRegDto);
		if (validForm != null)
			throw new ValidFormException(validForm);

		User userByUsername = userRepo.findByUsername(userRegDto.getUsername()).orElse(null);
		User userByEmail = userRepo.findByEmail(userRegDto.getEmail()).orElse(null);
		if (userByUsername != null)
			throw new ObjectAlreadyExistedException("Username đã tồn tại!");
		if (userByEmail != null)
			throw new ObjectAlreadyExistedException("Email đã tồn tại!");

		User newUser = new User();
		newUser.setEmail(userRegDto.getEmail());
		newUser.setFullName(userRegDto.getFullName());
		newUser.setPassword(encoder.encode(userRegDto.getPassword()));
		newUser.setUsername(userRegDto.getUsername());

		Role userRole = roleRepo.findByRoleName("USER").orElse(new Role("USER"));
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		newUser.setRoles(roles);
		userRepo.save(newUser);
		return Optional.of(newUser);
	}

	@Override
	public List<UserDto> findAll(int pageNo, int pageSize, String sortBy) {
		Page<User> userPage = getPageable(pageNo, pageSize, sortBy);
		if (userPage.hasContent()) {
			return userPage.getContent().stream()
					.map(mapperUser::fromUserToUserDto)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public Page<User> getPageable(int pageNo, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<User> userPage = userRepo.findAll(pageable);
		return userPage;
	}

	@Override
	public Message delete(Long id) {
		Optional<User> delUser = userRepo.findById(id);
		if (delUser.isPresent()) {
			User user = delUser.get();
			user.setEnabled(false);
			userRepo.save(user);
			return new Message("Đã enabled user ID: " + id);
		} else {
			throw new ObjectNotFoundException("Không tìm thấy thành viên");
		}

	}

	@Override
	public Optional<User> findById(Long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user;
		} else {
			throw new ObjectNotFoundException("Không tìm thấy thành viên");
		}
	}

	@Override
	public Message update(UserUpdateDto dto) {
		Optional<User> user = userRepo.findById(dto.getId());
		if (!user.isPresent())
			throw new ObjectNotFoundException("Không tìm thấy thành viên");
		User userUpdate = user.get();
		userUpdate.setEnabled(dto.isEnabled());
		Collection<Role> roles = dto.getRole_id().stream().map(x -> {
			return roleRepo.findById(x).get();
		}).collect(Collectors.toList());
		userUpdate.setRoles(roles);
		userRepo.save(userUpdate);
		return new Message("Thành công!");

	}

	@Override
	public UserUpdateDto getUpdateUser(Long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			UserUpdateDto dto = new UserUpdateDto();
			User userEntity = user.get();
			dto.setId(userEntity.getId());
			dto.setFullName(userEntity.getFullName());
			dto.setUsername(userEntity.getUsername());
			dto.setEnabled(userEntity.isEnabled());
			dto.setRole_id(user.get().getRoles().stream().map(Role::getId).collect(Collectors.toList()));
			return dto;
		} else {
			throw new ObjectNotFoundException("Không tìm thấy thành viên");
		}
	}

	@Override
	public User getCurrentLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user;
		try {
			user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	


	
	
}











