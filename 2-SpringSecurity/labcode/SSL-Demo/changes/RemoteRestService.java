package com.workshop.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemoteRestService {

  @Autowired
  private RestTemplate myRestTemplate;

  @Value("${myrest.url}")
  private String restUrl;

  @Value("${my.key}")
  private String restKey;
  
  
  public void checkRates(String currencyToCompare, String[] currenciesToConvert) {
    
    String finalUrl = restUrl + "/" + restKey + "/latest/" + currencyToCompare;
    log.info("GET to " + finalUrl);
    String jsonString = myRestTemplate.getForObject(finalUrl, String.class);
    log.info("Result in String form : " + jsonString);
    log.info("Currency to compare : " + currencyToCompare);

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode,conversionNode;
    try {
      rootNode = objectMapper.readTree(jsonString);
      conversionNode = rootNode.get("conversion_rates");
      if (conversionNode == null) {
        log.info("Could not find node: conversion_rates");
        return;
      }
      for (String currency : currenciesToConvert) {
        JsonNode tempNode = conversionNode.get(currency);
        if (tempNode != null) {
          log.info(currency + " : " + tempNode.asText());
        }
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }
  
  
}


