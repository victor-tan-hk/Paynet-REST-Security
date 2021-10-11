package com.workshop.security;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class DeveloperController {


  private static final Logger logger = LoggerFactory.getLogger(DeveloperController.class);

  @GetMapping("/developers")
  public Developer getDeveloper() {

    logger.info("GET /api/developer invoked");
    
    Developer dev = new Developer(1, "Spiderman", 33, new String[]{"Python", "Java"}, false);
    return dev;

  }

  @PostMapping("/developers")
  public void addSingleDeveloper(@RequestBody Developer dev) {

    logger.info("POST /api/developers invoked");
    logger.info("Developer specified : " + dev);

  }

  @PutMapping("/developers/{devIdString}")
  public void updateDeveloper(@PathVariable String devIdString, @RequestBody Developer dev) {

    logger.info("PUT /api/developers invoked");
    logger.info("Location specified : " + devIdString);
    logger.info("Developer specified : " + dev);

  }
  
  @DeleteMapping("/developers/{devIdString}")
  public void deleteDeveloper(@PathVariable String devIdString) {

    logger.info("DELETE /api/developers invoked");
    logger.info("Location specified : " + devIdString);

  }  
  

}
