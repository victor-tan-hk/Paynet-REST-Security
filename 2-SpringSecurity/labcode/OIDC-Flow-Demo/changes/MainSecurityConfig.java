package com.workshop.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@EnableWebSecurity
@Configuration
public class MainSecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    
    http.authorizeRequests()
      .antMatchers("/", "/login", "/oauth/**").permitAll()
      .anyRequest().authenticated()
    .and()
      .formLogin().loginPage("/login")
    .and()
       .oauth2Login().loginPage("/login")
     .and()
     	.logout().logoutUrl("/performLogout")
     	.logoutSuccessUrl("/login?logout=true");
     
      
  }


}

