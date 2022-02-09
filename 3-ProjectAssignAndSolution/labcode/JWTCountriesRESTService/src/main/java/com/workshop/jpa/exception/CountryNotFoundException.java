package com.workshop.jpa.exception;

public class CountryNotFoundException extends RuntimeException {
  
  public CountryNotFoundException(String message) {
    super(message);
  }

}
