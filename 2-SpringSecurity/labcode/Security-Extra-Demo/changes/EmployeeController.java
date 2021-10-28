package com.workshop.security;

import org.springframework.security.access.annotation.Secured;
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
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/add")
	public String addPage() {

		return "add";
	}
	
	// Only user role can access this method
	@Secured("ROLE_USER")
	@GetMapping("/view")
	public String viewPage() {

		return "view";
	}
	
	// Only admin role can access this method
	@Secured("ROLE_ADMIN")
	@GetMapping("/delete")
	public String deletePage() {

		return "delete";
	}

	@GetMapping("/update")
	public String updatePage() {

		return "update";
	}

 

}
