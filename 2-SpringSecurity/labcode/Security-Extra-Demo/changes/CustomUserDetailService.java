package com.workshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		final AppUser appUser = userRepository.findByUsername(username);
		if (appUser == null) {
			throw new UsernameNotFoundException(username);
		}
		log.info("Retrieved from password database : " + appUser.toString());
		UserDetails user = User.withUsername(appUser.getUsername()).password(appUser.getPassword()).authorities(appUser.getAuthority()).build();
		return user;
	}
}