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

    @Override
    public void run(String... args) throws Exception {
      
      log.info("Started MyRunner");
      
      waitKeyPress();
      
      log.info("Retrieving a single developer");
      Developer singleDev = myRestService.getSingleDeveloper();
      log.info(singleDev.toString());

      Developer dev1 = new Developer(0,"Flora",65,new String[] {"Erlang","C++","Java"},true);
      log.info("Posting a single developer");
      myRestService.addDeveloper(dev1);

      log.info("Modifying a single developer at id 888");
      myRestService.modifyDeveloper(dev1, 888);
      
      log.info("Deleting a single developer at id 888");
      myRestService.deleteDeveloper(888);

      log.info("Application complete !");
    }
    
    
    private void waitKeyPress() {
      log.info("Press enter to continue ....");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
      log.info("\n");
      //scanner.close();
    }
}