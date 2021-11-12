package com.workshop.security;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {

    @Autowired
    private MyRestService myRestService;
    
	@Autowired
	JWTClientProvider jwtClient;

    @Override
    public void run(String... args) throws Exception {
    	
    	
    	String jwt = null;
    	
    	log.info("Retrieving a developer in JSON directly from body");
    	Developer d = myRestService.getDeveloper();
    	log.info(d.toString());
    	
    	waitKeyPress();
    	
    	log.info("Retrieving a developer from a JWT that was signed with secret key");
    	jwt = myRestService.getDeveloperAsJWTwithSecret();
    	jwtClient.decodeJWT(jwt, jwtClient.getSecretKey());
    	
    	waitKeyPress();
    	
    	log.info("Retrieving a developer from a JWT that was signed with public key");
    	jwt = myRestService.getDeveloperAsJWTwithPrivate();
    	jwtClient.decodeJWT(jwt, jwtClient.getPublicKey());
    	
    	waitKeyPress();
    	
    	log.info("Sending a developer as JWT signed with private key in the query parameter");
    	Developer dev = new Developer("Black Panther",36, true);
    	jwt = jwtClient.createJWT(dev, jwtClient.getSecretKey());
    	myRestService.sendJwtInQueryParameter(jwt);
    	
    	waitKeyPress();
    	
    	log.info("Sending a developer as JWT signed with private key in the header");
    	dev = new Developer("Captain America",25, true);
    	jwt = jwtClient.createJWT(dev, jwtClient.getSecretKey());
    	myRestService.sendJwtInHeader(jwt);

    	waitKeyPress();
    	
    	log.info("Sending a developer as JWT signed with private key in the body");
    	dev = new Developer("Black Widow",35, true);
    	jwt = jwtClient.createJWT(dev, jwtClient.getSecretKey());
    	myRestService.sendJwtInBody(jwt);
    	
    	
    	log.info("Complete !");

    	

    }
    
    
    private void waitKeyPress() {
      log.info("Press enter to continue ....");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
      log.info("\n");
      //scanner.close();
    }
}