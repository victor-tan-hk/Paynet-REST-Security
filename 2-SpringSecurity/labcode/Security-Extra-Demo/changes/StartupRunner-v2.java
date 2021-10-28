package com.workshop.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import  org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

  @Autowired
  private AppUserRepository userRepo;
  
	
  @Override
  public void run(String... args) throws Exception {
	  
    log.info("Startup logic to populate user info database table");
    
    PasswordEncoder pwe = PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
    
    AppUser u1 = new AppUser(0L, "spiderman", pwe.encode("spider"), "ROLE_USER", true, new Date(),  "Peter Parker", 22);
    
    AppUser u2 = new AppUser(0L, "ironman", pwe.encode("iron"), "ROLE_USER", true, new Date(),  "Tony Stark", 45);
    
    AppUser u3 = new AppUser(0L, "superman", pwe.encode("super"), "ROLE_ADMIN", true, new Date(),  "Clark Kent", 33);
    
    AppUser u4 = new AppUser(0L, "wonderwoman", pwe.encode("wonder"), "ROLE_ADMIN", true, new Date(),  "Diana Prince", 3000); 
    		
    userRepo.save(u1);
    userRepo.save(u2);
    userRepo.save(u3);
    userRepo.save(u4);

  }



}