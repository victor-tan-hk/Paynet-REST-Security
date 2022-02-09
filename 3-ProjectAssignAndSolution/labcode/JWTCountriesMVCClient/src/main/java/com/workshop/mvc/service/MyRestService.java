package com.workshop.mvc.service;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.workshop.mvc.dto.CountryDTO;
import com.workshop.mvc.dto.PatchFormatDTO;
import com.workshop.mvc.dto.TotalDTO;
import com.workshop.mvc.exception.GetParamException;
import com.workshop.mvc.exception.PageListParamException;
import com.workshop.mvc.misc.ServerConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyRestService {

  @Autowired
  private RestTemplate myRestTemplate;

  @Value("${myrest.url}")
  private String restUrl;
  
  private String jwtString = null;

  private String limit = null;
  private String page = null;
  
  
  public void setJwtString(String jwtString) {
    this.jwtString = jwtString;
  }
  
  public String getJwtString() {
    return jwtString;
  }

  public void setLimitAndPage(String limitStr, String pageStr) throws PageListParamException {

    if (limitStr.length() == 0 || pageStr.length() == 0)
      throw new PageListParamException("The limit and page options must be specified together");

    long tempValue = -1;
    try {
      tempValue = Long.parseLong(limitStr);
    } catch (NumberFormatException ex) {
      throw new PageListParamException("Limit option must have a numeric value");
    }

    if (tempValue < 1) {
      throw new PageListParamException("Limit option must be 1 or higher");
    }

    this.limit = limitStr;

    tempValue = -1;
    try {
      tempValue = Long.parseLong(pageStr);
    } catch (NumberFormatException ex) {
      throw new PageListParamException("Page option must have a numeric value");
    }

    if (tempValue < 0) {
      throw new PageListParamException("Page option must be 0 or higher");
    }

    this.page = pageStr;

  }

  public List<CountryDTO> getCountryList(String key, String value)
      throws GetParamException, HttpClientErrorException {

    List<CountryDTO> listToDisplay = new ArrayList<CountryDTO>();
    String finalUrl = "";

    if (key.equals(ServerConstants.NAME_PARAM)) {

      if (value.contains(ServerConstants.INCLUDE_ALL_PARAM))
        finalUrl = restUrl;
      else {

        finalUrl = restUrl + "/" + value;
        log.info("GET to " + finalUrl);
  
        // For a GET, send empty string in body because this is not used
        HttpEntity<String> requestEntity = new HttpEntity<String>("",getRequestHeaders());
        ResponseEntity<CountryDTO> response = myRestTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity, CountryDTO.class);
        CountryDTO country = response.getBody();

//        CountryDTO country = myRestTemplate.getForObject(finalUrl, CountryDTO.class);
        if (country != null)
          listToDisplay.add(country);
        return listToDisplay;

      }

    } else if (key.equals(ServerConstants.CAPITAL_PARAM)) {

      finalUrl = restUrl + "/" + ServerConstants.CAPITAL_PARAM + "/" + value;
      log.info("GET to " + finalUrl);
      
      // For a GET, send empty string in body because this is not used
      HttpEntity<String> requestEntity = new HttpEntity<String>("",getRequestHeaders());
      ResponseEntity<CountryDTO> response = myRestTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity, CountryDTO.class);
      CountryDTO country = response.getBody();

      //CountryDTO country = myRestTemplate.getForObject(finalUrl, CountryDTO.class);
      if (country != null)
        listToDisplay.add(country);
      return listToDisplay;

    } else if (key.equals(ServerConstants.REGION_PARAM)) {

      finalUrl = restUrl + "?" + ServerConstants.REGION_PARAM + "=" + value;

    } else if (key.equals(ServerConstants.SUBREGION_PARAM)) {

      finalUrl = restUrl + "?" + ServerConstants.SUBREGION_PARAM + "=" + value;

    } else if (key.equals(ServerConstants.CURRENCY_PARAM)) {

      finalUrl = restUrl + "?" + ServerConstants.CURRENCY_PARAM + "=" + value;

    } else if (key.equals(ServerConstants.LANGUAGE_PARAM)) {

      finalUrl = restUrl + "?" + ServerConstants.LANGUAGE_PARAM + "=" + value;

    } else if (key.equals(ServerConstants.LANGUAGE_NUMBER_PARAM)) {

      finalUrl = restUrl + "?" + ServerConstants.LANGUAGE_NUMBER_PARAM + "=" + value;

    } else if (key.equals(ServerConstants.POPULATION_MORE_PARAM)) {

      try {
        Integer.parseInt(value);
      } catch (NumberFormatException ex) {
        throw new GetParamException("Population specified must be a numeric value");
      }

      finalUrl = restUrl + "?" + ServerConstants.POPULATION_PARAM + ">" + value;

    } else if (key.equals(ServerConstants.POPULATION_LESS_PARAM)) {

      try {
        Integer.parseInt(value);
      } catch (NumberFormatException ex) {
        throw new GetParamException("Population specified must be a numeric value");
      }

      finalUrl = restUrl + "?" + ServerConstants.POPULATION_PARAM + "<" + value;

    } else if (key.equals(ServerConstants.SORTING_PARAM)) {

      if (!(value.equals(ServerConstants.POPULATION_ASCENDING)
          || value.equals(ServerConstants.POPULATION_DESCENDING))) {
        throw new GetParamException("Option " + ServerConstants.SORTING_PARAM + " must be either "
            + ServerConstants.POPULATION_ASCENDING + " or " + ServerConstants.POPULATION_DESCENDING);
      }

      finalUrl = restUrl + "?" + ServerConstants.SORTING_PARAM + "=" + value;

    }

    if (limit != null && page != null) {

      if (finalUrl.contains("="))
        finalUrl += "&" + ServerConstants.LIMIT_PARAM + "=" + limit + "&" + ServerConstants.PAGE_PARAM + "=" + page;
      else
        finalUrl += "?" + ServerConstants.LIMIT_PARAM + "=" + limit + "&" + ServerConstants.PAGE_PARAM + "=" + page;

    }

    limit = null;
    page = null;

    log.info("GET to " + finalUrl);

    // For a GET, send empty string in body because this is not used
    HttpEntity<String> requestEntity = new HttpEntity<String>("",getRequestHeaders());
    ResponseEntity<List<CountryDTO>> countryResponse = myRestTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity,
        new ParameterizedTypeReference<List<CountryDTO>>() {
        });

    if (countryResponse != null)
      listToDisplay = countryResponse.getBody();
    return listToDisplay;

  }

  public TotalDTO getTotalCount(String key, String value) throws HttpClientErrorException {

    String finalUrl = restUrl + "/count?" + key + "=" + value;
    log.info("GET to " + finalUrl);
    
    HttpEntity<String> requestEntity = new HttpEntity<String>("",getRequestHeaders());
    ResponseEntity<TotalDTO> response = myRestTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity, TotalDTO.class);
    TotalDTO result = response.getBody();

    //TotalDTO result = myRestTemplate.getForObject(finalUrl, TotalDTO.class, new HashMap<String, String>());

    return result;
  }

  public void postCountry(CountryDTO country) throws HttpClientErrorException {

    log.info("POST to " + restUrl);
    log.info("With contents : " + country.toString());
    
    HttpEntity<CountryDTO> requestEntity = new HttpEntity<CountryDTO>(country, getRequestHeaders());
    myRestTemplate.exchange(restUrl, HttpMethod.POST, requestEntity, String.class);

//    myRestTemplate.postForLocation(restUrl, country, new HashMap<String, String>());

  }

  public void putCountry(CountryDTO country) throws HttpClientErrorException {

    String finalURL = restUrl + "/" + country.getName();

    log.info("PUT to " + finalURL);
    log.info("With contents : " + country.toString());
    
    HttpEntity<CountryDTO> requestEntity = new HttpEntity<CountryDTO>(country, getRequestHeaders());
    myRestTemplate.exchange(finalURL, HttpMethod.PUT, requestEntity, String.class);

    //myRestTemplate.put(finalURL, country, new HashMap<String, String>());

  }

  
  public void patchCountry(String countryName, String fileName) {

    log.info("Reading JSON contents from file : " + fileName);

    ObjectMapper objectMapper = new ObjectMapper();
    PatchFormatDTO[] patchContents = new PatchFormatDTO[0];
    try {
      patchContents = objectMapper.readValue(new File(fileName), PatchFormatDTO[].class);
    } catch (IOException e) {
      e.printStackTrace();
      log.info("Error in parsing JSON from file");
      System.exit(-1);
    }

    String finalURL = restUrl + "/" + countryName;

    HttpHeaders headers = getRequestHeaders();
    headers.set("Content-Type", "application/json-patch+json");
    HttpEntity<PatchFormatDTO[]> requestEntity = new HttpEntity<>(patchContents, headers);

    log.info("PATCH to " + finalURL);
    log.info("Contents of PATCH");
    for (PatchFormatDTO pfdto : patchContents) {
      log.info(pfdto.toString());
    }
    ResponseEntity<CountryDTO> response = null;

    try {
      response = myRestTemplate.exchange(finalURL, HttpMethod.PATCH, requestEntity, CountryDTO.class);
    } catch (HttpClientErrorException hcee) {
      log.info("Error message returned from server side");
      log.info(hcee.getResponseBodyAsString());
      System.exit(-1);
    }

    CountryDTO country = response.getBody();
    log.info("Modified content received in return : ");
    log.info(country.toString());

  }

  public void deleteCountry(String name) throws HttpClientErrorException {

    String finalUrl = restUrl + "/" + name;
    log.info("DELETE to " + finalUrl);
    
    HttpEntity<String> requestEntity = new HttpEntity<String>("", getRequestHeaders());
    myRestTemplate.exchange(finalUrl, HttpMethod.DELETE, requestEntity, String.class);

    //myRestTemplate.delete(finalUrl, new HashMap<String, String>());
  }
  
  // Add a Authorization header that includes the JWT after the bearer keyword
  private HttpHeaders getRequestHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + jwtString);
    return headers;
  }
  

  private String checkNumericOptions(String optionName, String value) {

    long tempValue = -1;
    try {
      tempValue = Long.parseLong(value);
    } catch (NumberFormatException ex) {
      return "Option " + optionName + " must be a numeric value";
    }

    if (tempValue < 0) {
      return "Option " + optionName + "  must be 0 or higher";
    }

    return null;

  }

  /*
   * Use to ensure that a DTO object has all its fields initialized properly
   * before use
   */
  private boolean isCountryValid(CountryDTO country) {

    return (country.getName() != null && country.getCapital() != null && country.getRegion() != null
        && country.getSubregion() != null && country.getPopulation() >= 0 && country.getCurrencies() != null
        && country.getLanguages() != null);

  }

  /*
   * Read the JSON content from a file, serialize it into a CountryDTO object,
   * check its validity, then return it.
   */
  private CountryDTO readCountryJSONFromFile(String fileName) {

    log.info("Reading JSON contents from file : " + fileName);

    ObjectMapper objectMapper = new ObjectMapper();
    CountryDTO country = new CountryDTO();
    try {
      country = objectMapper.readValue(new File(fileName), CountryDTO.class);
    } catch (IOException e) {
      e.printStackTrace();
      log.info("Error in parsing JSON from file");
      System.exit(-1);
    }

    if (!isCountryValid(country)) {
      log.info("JSON in file does not conform to expected format for a country");
      System.exit(-1);
    }

    return country;
  }

}