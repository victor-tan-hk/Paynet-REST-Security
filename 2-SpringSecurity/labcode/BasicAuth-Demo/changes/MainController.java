package com.workshop.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {


	@GetMapping("/first")
	public Developer firstMapping() {

		log.info("/first invoked");

		Developer dev = new Developer("Spiderman", 22, false);
		return dev;

	}
	
	@GetMapping("/second")
	public Developer secondMapping() {

		log.info("/second invoked");

		Developer dev = new Developer("Ironman", 33, true);
		return dev;

	}
	
	@GetMapping("/third")
	public Developer thirdMapping() {

		log.info("/third invoked");

		Developer dev = new Developer("Superman", 44, true);
		return dev;

	}


}
