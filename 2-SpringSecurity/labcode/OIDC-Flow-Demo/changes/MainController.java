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


@Controller
@Slf4j
public class MainController {
  


  @GetMapping("/view") 
  public String viewPage() {
    
    log.info("From inside view");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    OidcUser user = (OidcUser) auth.getPrincipal();
    log.info("name : " + user.getName());
    log.info("email : " + user.getEmail());
    log.info("family name : " + user.getFamilyName());
    log.info("full name : " + user.getFullName());
    log.info("given name : " + user.getGivenName());
    log.info("nick name : " + user.getNickName());
    log.info("gender : " + user.getGender());
    
    Collection<? extends GrantedAuthority> grantedAuthorities= user.getAuthorities();
    log.info("The number of authorities is : " + grantedAuthorities.size() );
    
    for (GrantedAuthority ga : grantedAuthorities) {
      log.info("Authority is : " + ga.getAuthority());
    }

    OidcIdToken idToken = user.getIdToken();
    Map<String,Object> claims = idToken.getClaims();
    log.info("Showing claims in idToken");
    for (Map.Entry<String, Object> entry : claims.entrySet()) {
      log.info(entry.getKey() + " : " + entry.getValue());
    }
    
    return "view";
  }
  
}