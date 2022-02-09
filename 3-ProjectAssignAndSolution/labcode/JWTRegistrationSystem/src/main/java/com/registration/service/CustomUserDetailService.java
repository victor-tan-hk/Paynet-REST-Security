package com.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.registration.model.RegUser;
import com.registration.repository.RegUserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private RegUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		final RegUser regUser = userRepository.findByEmail(userEmail);
		if (regUser == null) {
			throw new UsernameNotFoundException(userEmail);
		}
		log.info("Retrieved user account info from database : " + regUser.toString());
		UserDetails user = User.withUsername(regUser.getEmail()).password(regUser.getPassword()).authorities(regUser.getRoleMVC()).build();
		return user;
	}
}