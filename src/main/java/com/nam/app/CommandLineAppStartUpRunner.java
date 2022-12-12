package com.nam.app;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nam.entity.Role;
import com.nam.entity.User;
import com.nam.repository.IRoleRepository;
import com.nam.repository.IUserRepository;

//@Component
public class CommandLineAppStartUpRunner implements CommandLineRunner {

	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {

		Role userRole = new Role("USER");
		Role adminRole = new Role("ADMIN");

		for (int i = 0; i < 17; i++) {
			User user = new User();
			if (i % 3 == 0)
				user.setEnabled(true);
//			if (i % 4 == 0)
//				user.setRoles(Arrays.asList(adminRole));
			user.setEmail("email" + i);
			user.setFullName("nam" + i);
			user.setUsername("nam" + i);
			user.setPassword("pass" + i);
//			user.setRoles(Arrays.asList(userRole));
			userRepo.save(user);

		}

	}

}
