package com.nam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nam.entity.User;
import com.nam.repository.IUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userRepo.findByUsername(username).orElse(null);
			if (user == null)
				throw new UsernameNotFoundException("Khong tim thay user name: " + username);
			return new UserDetailsImpl(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
