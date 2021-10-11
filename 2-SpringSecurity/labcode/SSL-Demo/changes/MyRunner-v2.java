package com.workshop.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyRunner implements CommandLineRunner {


    @Autowired
    private RemoteRestService myRestService;    
    
    @Override
    public void run(String... args) throws Exception {
      
      log.info("Started MyRunner");
      
      String currencyToCompare = "MYR";
      
      String[] currenciesToConvert = {"USD","GBP", "MYR", "SGD", "AUD"};
      
      myRestService.checkRates(currencyToCompare, currenciesToConvert);
      

    }
}

