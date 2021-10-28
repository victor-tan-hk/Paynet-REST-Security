package com.workshop.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class MainSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()

				.antMatchers("/","/login").permitAll()
				
				.anyRequest().authenticated()
				
				.and()
				
				.sessionManagement().invalidSessionUrl("/login")
								
				.and()

				.formLogin().loginPage("/login").permitAll().loginProcessingUrl("/performLogin")

				.and()

				.logout().logoutUrl("/performLogout").logoutSuccessUrl("/login").permitAll();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		SecureRandom secureRandom = null;
		try {
			secureRandom = SecureRandom.getInstance("Windows-PRNG");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, PasswordEncoder> encoders = new HashMap<>();

		encoders.put("pbkdf2-weak", new Pbkdf2PasswordEncoder("mysecret", 8, 5000, 128));
		encoders.put("pbkdf2-strong", new Pbkdf2PasswordEncoder("mysecret", 32, 200000, 512));

		encoders.put("bcrypt-weak", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 4));

		encoders.put("bcrypt-strong",
				new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 31, secureRandom));

		return new DelegatingPasswordEncoder("pbkdf2-weak", encoders);
	}

}
