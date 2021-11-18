package com.workshop.security;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final static String JwtIssuer = "Marvel Universe";
	
	
  @Autowired
  private AuthenticationManager authenticationManager;

  private JwtTokenUtil jwtTokenUtil;

 
  @Autowired
  public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil) {
	  log.info("AuthenticationController started"); 
	  Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	  this.jwtTokenUtil = jwtTokenUtil;
	  jwtTokenUtil.setKeyToUse(secretKey);
  }

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
	
	log.info("Performing authentication of submitted username / password using configured AuthenticationManager from MainSecurityConfig");
	
	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

	
	log.info("Authenticated user is : " + authentication.getName());
	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	String userRole = userDetails.getAuthorities().iterator().next().getAuthority();
	log.info("User has authority : " + userRole);
	
	Map<String, Object> claimMap = new HashMap<String,Object>();
	claimMap.put("role", userRole);

    String token = jwtTokenUtil.generateToken(claimMap, authentication.getName(),JwtIssuer);
     
    log.info("Token : " + token);

    return ResponseEntity.ok(new JwtResponse(token));
  }

}