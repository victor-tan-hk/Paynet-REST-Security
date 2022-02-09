package com.registration;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	
	private final static String userRole = "USER";
	private final static String adminRole = "ADMIN";
	
   
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
    
//    This allows us to access all the CSS files placed in
//    src/main/resources/public/css without any access control 
//    enforcement
    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring()
           .antMatchers("/css/**"); 
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.authorizeRequests()
    		.antMatchers("/","/login", "/oauth/**").permitAll()
    		.antMatchers("/register", "/create-success").permitAll()
    		.antMatchers("/maintain","/changedate").hasRole(adminRole)
    		.anyRequest().authenticated()

   		.and()

       	.formLogin()
         	.loginPage("/login")
         	.loginProcessingUrl("/performLogin")
         	.defaultSuccessUrl("/mainmenu")
    			
   		.and()

   			.oauth2Login()
   				.loginPage("/login")
   				.defaultSuccessUrl("/mainmenu")

   		.and()
   		
       	.logout()
     			.logoutUrl("/performLogout")
     			.logoutSuccessUrl("/login?logout=true");
    		
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    	
    	return new BCryptPasswordEncoder();
        
    }

}
