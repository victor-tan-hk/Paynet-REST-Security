package com.workshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;



@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final static String user1Name = "spiderman";
	private final static String user1Password = "spider";

	private final static String user2Name = "ironman";
	private final static String user2Password = "iron";

	private final static String user3Name = "superman";
	private final static String user3Password = "super";

	private final static String user4Name = "wonderwoman";
	private final static String user4Password = "wonder";
	
	private final static String role1 = "USER";
	private final static String role2 = "ADMIN";
	
	private final static String rolePrefix = "ROLE_";


    public SecurityConfig() {
        super();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).
        withUser(user1Name).password(passwordEncoder().encode(user1Password)).roles(role1).and().
        withUser(user2Name).password(passwordEncoder().encode(user2Password)).roles(role1).and().
        withUser(user3Name).password(passwordEncoder().encode(user3Password)).roles(role2).and().
        withUser(user4Name).password(passwordEncoder().encode(user4Password)).roles(role2);
		
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable()
        	.authorizeRequests()
	    		.antMatchers("/api/first").hasAnyRole(role1, role2) 
	    		.antMatchers("/api/second").hasRole(role1) 
	    		.antMatchers("/api/third").hasRole(role2) 
        		.anyRequest().authenticated()
        	.and()
        		.httpBasic()
            .and()
                .exceptionHandling()
                	.accessDeniedHandler(accessDeniedHandler())	
                	.authenticationEntryPoint(authenticationEntryPoint())
    		.and()
    			.sessionManagement()
    			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);    	
               
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    	
    }

}
