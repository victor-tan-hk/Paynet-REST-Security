package com.registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.registration.model.RegUser;
import com.registration.repository.RegUserRepository;

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
  private RegUserRepository userRepo;
  
	
  @Override
  public void run(String... args) throws Exception {
	  
    log.info("Startup logic to populate user info database table");
    
    PasswordEncoder pwe = new BCryptPasswordEncoder();
    
    ArrayList<RegUser> initialUserList = new ArrayList<RegUser>();
    
    RegUser u1 = new RegUser(0L,"aa@aa.com", pwe.encode("aaa"), "ROLE_ADMIN", true, "Clark", "Kent", "ROLE_FULL");
    
    RegUser u2 = new RegUser(0L,"bb@bb.com", pwe.encode("bbb"), "ROLE_USER", true, "Tony", "Stark", "ROLE_BASIC");

    RegUser u3 = new RegUser(0L,"cc@cc.com", pwe.encode("ccc"), "ROLE_USER", true, "Bruce", "Banner", "ROLE_BASIC");

    initialUserList.add(u1);
    initialUserList.add(u2);
    initialUserList.add(u3);
    
    for (RegUser user : initialUserList) {
    	if (userRepo.findByEmail(user.getEmail()) == null)
    		userRepo.save(user);
    }

  }



}