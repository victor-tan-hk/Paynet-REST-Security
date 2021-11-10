package com.workshop.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@EnableWebSecurity
@Configuration
public class MainSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).
        withUser(StringConstantsHolder.user1Name).password(passwordEncoder().encode(StringConstantsHolder.user1Password)).roles(StringConstantsHolder.role1).and().
        withUser(StringConstantsHolder.user2Name).password(passwordEncoder().encode(StringConstantsHolder.user2Password)).roles(StringConstantsHolder.role1).and().
        withUser(StringConstantsHolder.user3Name).password(passwordEncoder().encode(StringConstantsHolder.user3Password)).roles(StringConstantsHolder.role2).and().
        withUser(StringConstantsHolder.user4Name).password(passwordEncoder().encode(StringConstantsHolder.user4Password)).roles(StringConstantsHolder.role2);
		
    }
	

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    
    http.authorizeRequests()
      .antMatchers("/", "/login", "/oauth/**").permitAll()
      
      // Anyone with role ADMIN or USER can access - this means essentially everyone
      .antMatchers("/view").hasAnyRole(StringConstantsHolder.role1, StringConstantsHolder.role2)

      // Only the GitHub or the Google user can access
      .antMatchers("/add").hasAnyAuthority("SCOPE_read:user", "SCOPE_openid")

      // Only the GitHub user can access
      .antMatchers("/update").hasAuthority("SCOPE_read:user")

      // Only role ADMIN can access
      .antMatchers("/delete").hasRole(StringConstantsHolder.role2)
      
      .anyRequest().authenticated()
    .and()
      .formLogin().loginPage("/login")
      .loginProcessingUrl("/performLogin")
    .and()
       .oauth2Login().loginPage("/login")
     .and()
     	.logout().logoutUrl("/performLogout")
     	.logoutSuccessUrl("/login?logout=true");
      
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

}

