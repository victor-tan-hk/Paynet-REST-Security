package com.workshop.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class EmployeeController {
	
	@Autowired
	IHeroService heroService;
	
	private long lastAccessTime = System.currentTimeMillis();
	
	
	@GetMapping("/")
	public String homePage() {
		lastAccessTime = System.currentTimeMillis();
		return "home";
	}
	
	
	
	@GetMapping("/login") 
	public String loginPage(HttpServletRequest request, Model model, @RequestParam(required=false) String error, @RequestParam(required=false) String logout) {
		
		HttpSession session = request.getSession();
		long now = System.currentTimeMillis();
		long timeDifference = now - lastAccessTime;
		log.info("Time difference is : " + timeDifference);
		long timeout = session.getMaxInactiveInterval() * 1000L;
		log.info("Timeout is : " + timeout);
		
        if (error != null)
            model.addAttribute("loginFailureMsg", "Your username or password are invalid.");
        else if (timeDifference < timeout )
            model.addAttribute("logoutMsg", "You have been logged out successfully.");
		else 
            model.addAttribute("timeoutMsg", "Session timed out due to inactivity. Please login again");
		
		return "login";
	}


	@GetMapping("/add")
	public String addHeroPowers() {
		
		lastAccessTime = System.currentTimeMillis();

		log.info("returning form for adding new powers");
		
		return "add";
	}

	
	@PostMapping("/add")
	public String processAdd(@RequestParam String heroname1, @RequestParam String power1,  
			@RequestParam String heroname2, @RequestParam String power2) {
		
		lastAccessTime = System.currentTimeMillis();
		
		log.info("process adding new hero powers");
		log.info("hero 1 name : " + heroname1);
		log.info("hero 1 new power : " + power1);
		log.info("hero 2 name : " + heroname2);
		log.info("hero 2 new power : " + power2);

		List<Hero> existingHeroes =  new ArrayList<Hero>();

		if (power1.length() > 0) {
			Hero hero1 = new Hero(heroname1);
			hero1.addNewPower(power1);
			existingHeroes.add(hero1);
		}
			
		if (power2.length() > 0) {
			Hero hero2 = new Hero(heroname2);
			hero2.addNewPower(power2);
			existingHeroes.add(hero2);
		}
		
		heroService.addHeroPowers(existingHeroes);
		
		return "home";
	}
	

	@GetMapping("/view")
	public String viewPage(Model model) {
		
		
		lastAccessTime = System.currentTimeMillis();
		
		log.info("returning view with list of heroes");
		
        model.addAttribute("heroes", heroService.getHeroes());

		return "view";
	}
	

	@GetMapping("/delete")
	public String deletePage() {
		
		lastAccessTime = System.currentTimeMillis();

		log.info("deletePage invoked");
		return "delete";
	}


	@GetMapping("/update")
	public String updatePage() {

		lastAccessTime = System.currentTimeMillis();

		log.info("updatePage invoked");
		return "update";
	}
 

}
