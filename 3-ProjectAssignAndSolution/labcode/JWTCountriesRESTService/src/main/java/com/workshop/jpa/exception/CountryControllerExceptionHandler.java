package com.workshop.jpa.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.workshop.jpa.dto.CustomErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CountryControllerExceptionHandler {

  
  private static final String urlToConsult = "https://www.awesomecompany.com/support/api/docs";

  
  // Handles exceptions related to processing of request
  @ExceptionHandler({IncorrectJSONFormatException.class, IncorrectURLFormatException.class, CountryNotFoundException.class}) 
  public ResponseEntity<CustomErrorMessage> handleCustomException(RuntimeException rte) {
    
    log.info("Exception handling logic to handle incorrect JSON / URL format or country not located");
    
    HttpStatus statusToReturn; 
    if (rte.getClass().equals(CountryNotFoundException.class)) {
      statusToReturn = HttpStatus.NOT_FOUND;
    }
    else 
      statusToReturn = HttpStatus.BAD_REQUEST;
    String messageToReturn = rte.getMessage();

    // Additional specialized info to return
    int localErrorId = 666;
    
    CustomErrorMessage newMessage = new CustomErrorMessage(new Date(), statusToReturn.value(), statusToReturn.toString(), messageToReturn, localErrorId, urlToConsult);
    
    return new ResponseEntity<CustomErrorMessage>(newMessage, statusToReturn);
  }
    
  
  // Handles Spring framework exceptions
  @ExceptionHandler(HttpMessageNotReadableException.class) 
  public ResponseEntity<CustomErrorMessage> handleSpringFrameworkException (HttpMessageNotReadableException mnrex) {
    
    log.info("Exception handling logic to handle Spring framework exceptions");

    HttpStatus statusToReturn = HttpStatus.BAD_REQUEST;
    String messageToReturn = "Your JSON was malformed. Please check it again";

    // Additional specialized info to return
    int localErrorId = 111;
    
    CustomErrorMessage newMessage = new CustomErrorMessage(new Date(), statusToReturn.value(), statusToReturn.toString(), messageToReturn, localErrorId, urlToConsult);
    
    return new ResponseEntity<CustomErrorMessage>(newMessage, statusToReturn);    
    
  }
  
  
}
