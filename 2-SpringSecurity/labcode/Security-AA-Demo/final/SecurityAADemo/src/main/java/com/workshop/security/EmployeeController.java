package com.workshop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class EmployeeController {
	
	
	@GetMapping("/add")
	public String addPage(Model model, Principal principal) {
		
		String activeUserName = principal.getName();
		// Business logic that uses activeUserName
		
		log.info("Returning add page to : " + activeUserName);
		model.addAttribute("userName",activeUserName);
		return "add";
	}
	
	
	
	@GetMapping("/delete/some") 
	public String deleteSomePage(Model model, Authentication auth) {
		
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String activeUserName = userDetails.getUsername();	
		Collection<? extends GrantedAuthority> userAuthorities = userDetails.getAuthorities();
		String activeUserAuthority = userAuthorities.iterator().next().getAuthority();
		// Business logic that uses activeUserName and activeUserAuthority

		log.info("Returning deletesome page to : " + activeUserName);
		model.addAttribute("userName",activeUserName);
		model.addAttribute("userAuthority",activeUserAuthority);
		
		return "deletesome";
	}

	@GetMapping("/login") 
	public String loginPage(Model model, @RequestParam(required=false) String error, @RequestParam(required=false) String logout) {
		
		log.info("Returning login page");
		
        if (error != null)
            model.addAttribute("errorMsg", "Unsuccessful login attempt.Try again");

        if (logout != null)
            model.addAttribute("logoutMsg", "You have been logged out successfully.");
		
		return "customLogin";
	}
 

}
