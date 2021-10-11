package com.workshop.security;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyRestService {

  private static final Logger logger = LoggerFactory.getLogger(MyRestService.class);

  @Autowired
  private RestTemplate myRestTemplate;

  @Value("${myrest.url}")
  private String restUrl;
  
  
  // GET /api/developers
  public Developer getSingleDeveloper() {
    
    String finalUrl = restUrl;
    logger.info("GET to " + finalUrl);
    return myRestTemplate.getForObject(finalUrl, Developer.class);
    
  }
  
  // POST  /api/developers
  public void addDeveloper(Developer dev) {
    
    String finalUrl = restUrl;
    logger.info("POST to " + finalUrl);
    
    URI destination = null;
    try {
      destination = new URI(finalUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    
    myRestTemplate.postForObject(destination, dev, String.class);

  }
  
  // PUT /api/developers/{id}
  public void modifyDeveloper(Developer dev, int id) {
    
    String finalUrl = restUrl + "/" + id;
    logger.info("PUT to " + finalUrl); 
    
    URI destination = null;
    try {
      destination = new URI(finalUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    myRestTemplate.put(destination, dev);

  }
  
  // DELETE  /api/developers/{id}
  public void deleteDeveloper(int id) {
    
    String finalUrl = restUrl + "/" + id;
    logger.info("DELETE to " + finalUrl);
    
    URI destination = null;
    try {
      destination = new URI(finalUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    myRestTemplate.delete(destination);
    
  }
   
  
}