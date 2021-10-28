package com.workshop.security;

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

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class EmployeeController {
	
	@Autowired
	IHeroService heroService;
	
	

	@GetMapping("/add")
	public String addHeroPowers() {

		log.info("returning form for adding new powers");
		
		return "add";
	}
	
	@PostMapping("/add")
	public String processAdd(@RequestParam String heroname1, @RequestParam String power1,  
			@RequestParam String heroname2, @RequestParam String power2) {
		
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
		
		log.info("returning view with list of heroes");
		
        model.addAttribute("heroes", heroService.getHeroes());

		return "view";
	}
	

	@GetMapping("/delete")
	public String deletePage() {

		log.info("deletePage invoked");
		return "delete";
	}


	@GetMapping("/update")
	public String updatePage() {

		log.info("updatePage invoked");
		return "update";
	}
 

}
