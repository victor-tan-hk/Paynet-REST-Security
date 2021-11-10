package com.workshop.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import org.springframework.security.core.userdetails.UserDetails;

@Controller
@Slf4j
public class MainController {

	@Autowired
	CustomUserDetailsService userDetailsService;

	@GetMapping("/")
	public String homePage(Model model) {

		log.info("From inside home");

		String realUserName = null;
		String userEmail = null;
		String userCompany = null;
		String userLocation = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();

		// This if condition must come first since
		// DefaultOidcUser is a subclass of DefaultOAuth2User
		if (principal instanceof DefaultOidcUser) {

			log.info("Login from Google");

			DefaultOidcUser user = (DefaultOidcUser) auth.getPrincipal();
			realUserName = user.getFullName();
			userEmail = user.getEmail();
			
		    Collection<? extends GrantedAuthority> grantedAuthorities= user.getAuthorities();
		    log.info("The number of authorities is : " + grantedAuthorities.size() );
		    
		    for (GrantedAuthority ga : grantedAuthorities) {
		      log.info("Authority is : " + ga.getAuthority());
		    }
			

		} else if (principal instanceof DefaultOAuth2User) {

			log.info("Login in from GitHub");

			DefaultOAuth2User user = (DefaultOAuth2User) principal;
			Map<String, Object> myAttributes = user.getAttributes();

			realUserName = (String) myAttributes.get("name");
			userEmail = (String) myAttributes.get("email");
			userLocation = (String) myAttributes.get("location");
			userCompany = (String) myAttributes.get("company");
			
		    Collection<? extends GrantedAuthority> grantedAuthorities= user.getAuthorities();
		    log.info("The number of authorities is : " + grantedAuthorities.size() );
		    
		    for (GrantedAuthority ga : grantedAuthorities) {
		      log.info("Authority is : " + ga.getAuthority());
		    }

		} else if (principal instanceof UserDetails) {

			log.info("Login from standard account");

			UserDetails user = (UserDetails) principal;
			String username = user.getUsername();
			realUserName = userDetailsService.getRealUserName(username);
			userEmail = userDetailsService.getUserEmail(username);
			userLocation = userDetailsService.getUserLocation(username);
			userCompany = userDetailsService.getUserCompany(username);
		}

		model.addAttribute("realUserName", realUserName);
		model.addAttribute("userEmail", userEmail);
		model.addAttribute("userLocation", userLocation);
		model.addAttribute("userCompany", userCompany);

		return "home";

	}


}