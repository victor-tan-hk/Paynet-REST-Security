package com.workshop.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	// Both admin and user roles can access this method
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/add")
	public String addPage() {

		log.info("addPage invoked");
		return "add";
	}
	
	// Only user role can access this method
	@PostAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/view")
	public String viewPage() {

		log.info("viewPage invoked");
		return "view";
	}
	
	// Only username ironman can access this method
	@PreAuthorize("authentication.principal.username == 'ironman'")
	@GetMapping("/delete")
	public String deletePage() {

		log.info("deletePage invoked");
		return "delete";
	}

	// Will only be authorized
	// if the return result meets the specified condition
	@PostAuthorize("returnObject == 'update'")
	@GetMapping("/update")
	public String updatePage() {

		log.info("updatePage invoked");
		return "update";
	}
 

}
