package com.workshop.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  
  // Handles exceptions resulting from authentication errors
  @ExceptionHandler({BadCredentialsException.class, DisabledException.class}) 
  public ResponseEntity<CustomErrorMessage> handleCustomException(Exception acex) {
    
    log.info("Handling authentication exception");

    HttpStatus statusToReturn = HttpStatus.UNAUTHORIZED;
    String messageToReturn = "Incorrect username / password combination or account disabled";

    // Additional specialized info to return
    int localErrorId = 666;
    String urlToConsult = "https://developer.twitter.com/en/support/twitter-api/error-troubleshooting";
    
    CustomErrorMessage newMessage = new CustomErrorMessage(new Date(), statusToReturn.value(), statusToReturn.toString(), messageToReturn, localErrorId, urlToConsult);
    
    return new ResponseEntity<CustomErrorMessage>(newMessage, statusToReturn);
  }
    

  
  
  
}
