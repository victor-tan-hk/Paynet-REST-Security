package com.workshop.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

	@Autowired
	private AppUserRepository userRepo;

	// Custom mapping for standard PasswordEncoder instances
	// instantiated with different constructor parameters
	private PasswordEncoder getPassWordEncoder(String algoId) {

		SecureRandom secureRandom = null;
		try {
			secureRandom = SecureRandom.getInstance("Windows-PRNG");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, PasswordEncoder> encoders = new HashMap<>();

		encoders.put("pbkdf2-weak", new Pbkdf2PasswordEncoder("mysecret", 8, 5000, 128));
		encoders.put("pbkdf2-strong", new Pbkdf2PasswordEncoder("mysecret", 32, 200000, 512));
				
		encoders.put("bcrypt-weak", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 4));
		
		encoders.put("bcrypt-strong", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 31, secureRandom));
		
		return new DelegatingPasswordEncoder(algoId, encoders);
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("Startup logic to populate user info database table");

		PasswordEncoder customPwe;

		// Generates a password encoded with scrypt
		customPwe = getPassWordEncoder("pbkdf2-weak");
		AppUser u1 = new AppUser(0L, "spiderman", customPwe.encode("spider"), "ROLE_USER", true, new Date(),
				"Peter Parker", 22);

		// Generates a password encoded with bcrypt
		customPwe = getPassWordEncoder("pbkdf2-strong");
		AppUser u2 = new AppUser(0L, "ironman", customPwe.encode("iron"), "ROLE_USER", true, new Date(), "Tony Stark",
				45);

		// Generates a password encoded with pbkdf2
		customPwe = getPassWordEncoder("bcrypt-weak");
		AppUser u3 = new AppUser(0L, "superman", customPwe.encode("super"), "ROLE_ADMIN", true, new Date(),
				"Clark Kent", 33);

		// Generates a password encoded with scrypt
		customPwe = getPassWordEncoder("bcrypt-strong");
		AppUser u4 = new AppUser(0L, "wonderwoman", customPwe.encode("wonder"), "ROLE_ADMIN", true, new Date(),
				"Diana Prince", 3000);

		userRepo.save(u1);
		userRepo.save(u2);
		userRepo.save(u3);
		userRepo.save(u4);

	}

}