package com.workshop.security;

import java.security.Key;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {

	@Autowired
	JWTServiceProvider jwtService;

	@GetMapping("/original")
	public Developer getSingleDeveloper() {

		log.info("/original invoked");

		Developer dev = new Developer("Spiderman", 22, false);
		return dev;

	}

	@GetMapping("/getjwtsecret")
	public JwtResponse getDeveloperAsJwtSignedWithSecret() {

		log.info("/getjwtsecret invoked");

		Developer dev = new Developer("Superman", 33, true);

		String jwt = jwtService.createJWT(dev, jwtService.getSecretKey());

		return new JwtResponse(jwt);

	}

	@GetMapping("/getjwtprivate")
	public JwtResponse getDeveloperAsJwtSignedWithPrivate() {

		log.info("/getjwtprivate invoked");

		Developer dev = new Developer("Ironman", 45, false);

		String jwt = jwtService.createJWT(dev, jwtService.getPrivateKey());

		return new JwtResponse(jwt);

	}

	// Retrieving JWT in jwt query parameter using @RequestParam
	@GetMapping("/receiveparam")
	public void processQueryParam(@RequestParam String jwt) {

		log.info("/receiveparam invoked");

		log.info("Received signed JWT as parameter : " + jwt);

		jwtService.decodeJWT(jwt, jwtService.getSecretKey());

	}

	// Retrieving JWT in Authorization header using @RequestHeader
	@GetMapping("/receiveheader")
	public void processHeader(@RequestHeader("Authorization") String jwt) {

		log.info("/receiveheader invoked");

		log.info("Received in authorization header " + jwt);

		jwtService.decodeJWT(jwt, jwtService.getSecretKey());

	}
	
	// Retrieving JWT from JSON content in HTTP body
	@PostMapping("/receivebody") 
	public void processBody(@RequestBody JwtResponse res) {
		
		log.info("/receivebody invoked");

		log.info("Received in HTTP body " + res);
		
		jwtService.decodeJWT(res.getJwt(), jwtService.getSecretKey());


		
	}

}
