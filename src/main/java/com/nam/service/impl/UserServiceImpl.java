package com.nam.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nam.dto.EmailDto;
import com.nam.dto.UserDto;
import com.nam.dto.UserRegistrationFormDto;
import com.nam.dto.UserUpdateDto;
import com.nam.entity.Order;
import com.nam.entity.ResetPasswordToken;
import com.nam.entity.Role;
import com.nam.entity.User;
import com.nam.event.ResetPasswordEvent;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectAlreadyExistedException;
import com.nam.exception_mesage.ObjectCanNotBeDelete;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.exception_mesage.ValidFormException;
import com.nam.mapper.IUserMapper;
import com.nam.repository.IRoleRepository;
import com.nam.repository.IUserRepository;
import com.nam.security.UserDetailsImpl;
import com.nam.service.IEmailService;
import com.nam.service.IUserService;
import com.nam.utils.FormValidation;
import com.nam.utils.UrlFromUser;

@Service
@PropertySource(value = "classpath:messages.properties", encoding = "utf-8")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private IEmailService emailService;
	@Autowired 
	private IUserMapper userMapper;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private Environment env;


	/* L???y danh s??ch t???t c??? user */
	@Override
	public List<User> findAll() {
		List<User> list = userRepo.findAll();
		return list;
	}

	/* Th???c hi???n ki???m tra ?????u v??o v?? l??u m???i 1 user v???a ????ng k?? */
	@Override
	public Optional<?> saveNewRegisterUser(UserRegistrationFormDto userRegDto) {
		// X??c th???c form ????ng k?? ng?????i d??ng m???i n???u c?? l???i th?? qu???ng ra exception
		String validForm = FormValidation.checkFormSignup(userRegDto);
		if (validForm != null)
			throw new ValidFormException(validForm);

		User userByUsername = userRepo.findByUsername(userRegDto.getUsername()).orElse(null);
		User userByEmail = userRepo.findByEmail(userRegDto.getEmail()).orElse(null);
		if (userByUsername != null)
			throw new ObjectAlreadyExistedException(env.getProperty("message.existed.username"));
		if (userByEmail != null)
			throw new ObjectAlreadyExistedException(env.getProperty("message.existed.email"));

		User saveUser = new User();
		saveUser.setEmail(userRegDto.getEmail());
		saveUser.setFullName(userRegDto.getFullName());
		saveUser.setPassword(encoder.encode(userRegDto.getPassword()));
		saveUser.setUsername(userRegDto.getUsername());

		Role userRole = roleRepo.findByRoleName("USER").orElse(new Role("USER"));
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		saveUser.setRoles(roles);
		userRepo.save(saveUser);
		return Optional.of(saveUser);
	}

	/* L???y ra danh s??ch user Dto d???a v??o tham s??? truy???n v??o */
	@Override
	public List<UserDto> findAll(int pageNo, int pageSize, String searchKey, Long roleId, int status) {
		Page<User> userPage = getPageable(pageNo, pageSize, searchKey, roleId, status);
		if (userPage.hasContent()) {
			return userPage.getContent().stream()
					.map(userMapper::fromUserToUserDto)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/* L???y ra page user d???a v??o tham s??? truy???n v??o */
	@Override
	public Page<User> getPageable(int pageNo, int pageSize, String searchKey, Long roleId, int status){
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Boolean enabled;
		if (status == 0)
			enabled = false;
		else if (status == 1)
			enabled = true;
		else
			enabled = null;
		
		Page<User> userPage = null;
	
		if (enabled != null && roleId != 0)
			userPage = userRepo.findAllBySearchKeyAndRoleAndStatus(searchKey, roleId, enabled, pageable);
		else if (enabled == null && roleId != 0)
			userPage = userRepo.findAllBySearchKeyAndRole(searchKey, roleId, pageable);
		else if (enabled != null && roleId == 0)
			userPage = userRepo.findAllBySearchKeyAndStatus(searchKey, enabled, pageable);
		else
			userPage = userRepo.findAllBySearchKey(searchKey, pageable);
		return userPage;
	}

	/* Nh???n v??o id user ????? x??a ng?????i d??ng v?? c??c order detail, c??c th??ng tin li??n quan */
	@Override
	public Message delete(Long id) {
		Optional<User> delUser = userRepo.findById(id);
		if (!delUser.isPresent())
			throw new ObjectNotFoundException(env.getProperty("message.not.find.user"));
		
		User user = delUser.get();
		
		boolean canDelete = !user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
				.contains("ADMIN");
		if (!canDelete) 
			throw new ObjectCanNotBeDelete(env.getProperty("message.can.not.delete.admin"));

		Collection<Order> orders= user.getOrders();
		orders.stream().forEach(order -> {
			order.getOrderDetails()
			.stream()
			.forEach(orderDetail -> {
				orderDetail.setBook(null);
			});
		});
		
		user.setRoles(null);
		userRepo.delete(user);
		return new Message(env.getProperty("message.delete.user.success"));
	}

	/* L???y user d???a v??o id */
	@Override
	public Optional<User> findById(Long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user;
		} else {
			throw new ObjectNotFoundException(env.getProperty("message.not.find.user"));
		}
	}

	/* Nh???n v??o user dto ????? update ng?????i d??ng */
	@Override
	public Message update(UserUpdateDto dto) {
		Optional<User> user = userRepo.findById(dto.getId());
		if (!user.isPresent())
			throw new ObjectNotFoundException(env.getProperty("message.not.find.user"));
		User userUpdate = user.get();
		userUpdate.setEnabled(dto.isEnabled());
		Collection<Role> roles = dto.getRole_id().stream().map(x -> {
			return roleRepo.findById(x).get();
		}).collect(Collectors.toList());
		userUpdate.setRoles(roles);
		userRepo.save(userUpdate);
		return new Message("Th??nh c??ng!");
	}
	
	/* L???y ra user c???n update d???a v??o id */
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
			throw new ObjectNotFoundException(env.getProperty("message.not.find.user"));
		}
	}

	/* Th???c hi???n ki???m tra ?????u v??o v??o thay ?????i m???t kh???u ng?????i d??ng */
	@Override
	public Message changePassword(String oldPassword, String newPassword1, String newPassword2) {
		User user = getCurrentLoggedInUser();
		if(!encoder.matches(oldPassword, user.getPassword()))
			throw new ValidFormException(env.getProperty("message.current.password.error"));
		if(!newPassword1.equals(newPassword2))
			throw new ValidFormException(env.getProperty("message.not.match.new.password"));
		String checkPassword = FormValidation.checkPassword(newPassword1);
		if(checkPassword != null)
			throw new ValidFormException(checkPassword);
		user.setPassword(encoder.encode(newPassword1));
		userRepo.save(user);
		return new Message(env.getProperty("message.change.password.success"));
	}

	/* L???y ra ng?????i d??ng ??ang ????ng nh???p */
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

	/* ki???m tra username v?? email xem c??? 2 c?? kh???p trong database hay kh??ng,
	 * n???u kh??ng th?? qu??ng ra l???i, n???u c?? th?? g???i 1 email ????? x??c nh???n c???p l???i m???t kh???u */
	@Override
	public Message sendEmailToConfirmBeforeResetPassowrd(String username, String email, HttpServletRequest http) {
		Optional<User> opUser = userRepo.findByUsername(username);
		if (opUser.isEmpty())
			throw new ObjectNotFoundException(env.getProperty("message.not.find.user") + username);
		User user = opUser.get();
		if (!user.getEmail().equalsIgnoreCase(email))
			throw new ObjectNotFoundException(env.getProperty("message.incorrect.recovery.email") + email);

		publisher.publishEvent(new ResetPasswordEvent(user, UrlFromUser.getUrl(http)));
		return new Message(env.getProperty("message.confirm.email.notification")
				+ user.getPasswordToken().getEXPIRATION()/60 + env.getProperty("message.minute"));
	}

	/* g???i m???t kh???u m???i v??? email ng?????i d??ng v?? m?? h??a m???t kh???u l??u v??o database */
	@Override
	public Message resetPassword(User user) {
		String newPassword = UUID.randomUUID().toString().substring(0, 8);
		user.setPassword(encoder.encode(newPassword));
		ResetPasswordToken passwordToken=user.getPasswordToken();
		passwordToken.resetToken();
		user.setPasswordToken(passwordToken);
		userRepo.save(user);
		
		EmailDto emailDto = new EmailDto();
		emailDto.setSubject(env.getProperty("message.email.supply.new.password") + user.getUsername());
		emailDto.setTo(user.getEmail());
		emailDto.setContent(env.getProperty("message.email.new.password.notification") 
				+ user.getUsername() + env.getProperty("message.email.is") + newPassword);
		emailService.sendConfirmationEmail(emailDto);
		return new Message(env.getProperty("message.send.new.password.success") + user.getEmail());
	}
}











