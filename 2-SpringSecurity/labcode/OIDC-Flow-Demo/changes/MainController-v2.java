package com.workshop.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.oauth2.core.user.OAuth2User;


@Controller
@Slf4j
public class MainController {
  


  @GetMapping("/view") 
  public String viewPage() {
    
    log.info("From inside view");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    OAuth2User user = (OAuth2User) auth.getPrincipal();
    log.info("name : " + user.getName());
    
    Collection<? extends GrantedAuthority> grantedAuthorities= user.getAuthorities();
    log.info("The number of authorities is : " + grantedAuthorities.size() );
    
    for (GrantedAuthority ga : grantedAuthorities) {
      log.info("Authority is : " + ga.getAuthority());
    }
    
    Map<String,Object> myAttributes = user.getAttributes();
    log.info("The number of attributes is : " + myAttributes.size() );
    
    for (Map.Entry<String, Object> entry : myAttributes.entrySet()) {
    	log.info("Key : " + entry.getKey() + " Value : " + entry.getValue());
    }

    
    return "view";
  }
  
}