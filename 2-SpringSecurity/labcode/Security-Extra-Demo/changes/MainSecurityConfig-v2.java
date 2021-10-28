package com.workshop.security;

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
			
    		.antMatchers("/").permitAll()
    		.anyRequest().authenticated() 
    		
        	.and()

        	.formLogin()
            	.loginPage("/login").permitAll()
    			.loginProcessingUrl("/performLogin")
 
    		.and()
       	
    		.logout()
       			.logoutUrl("/performLogout")
       			.logoutSuccessUrl("/login?logout=true").permitAll();
    	
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    	
    	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
    }

}
