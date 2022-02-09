package com.workshop.jpa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurityConfig extends WebSecurityConfigurerAdapter {
  
  private final static String basic_role = "BASIC";
  private final static String full_role = "FULL";

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

    public MainSecurityConfig() {
        super();
    }

    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

      httpSecurity
        // Disable CSRF for REST API
        .csrf().disable()

        .authorizeRequests()
        
//        Allow either FULL or BASIC role to retrieve and create new records
        .antMatchers(HttpMethod.GET, "/api/countries/**").hasAnyRole(basic_role, full_role)
        .antMatchers(HttpMethod.POST, "/api/countries/**").hasAnyRole(basic_role, full_role)
        
//        Only allow FULL role to update or delete existing records
        .antMatchers(HttpMethod.PUT, "/api/countries/**").hasRole(full_role)
        .antMatchers(HttpMethod.PATCH, "/api/countries/**").hasRole(full_role)
        .antMatchers(HttpMethod.DELETE, "/api/countries/**").hasRole(full_role)

        .anyRequest().authenticated()
        
      .and()
      
        // Set up custom handler to handle authorization exceptions
        .exceptionHandling()
          .accessDeniedHandler(accessDeniedHandler())         
      
      .and()
        // disable session tracking for REST API since JWS is submitted with each request
        // and we will not use cookies
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      
      // Add in the filter to extract JWT from incoming request prior to authorization processing
      httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                
    }

    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }    

}
