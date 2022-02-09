package com.workshop.jpa.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.fge.jsonpatch.JsonPatch;
import com.workshop.jpa.dto.CountryDTO;
import com.workshop.jpa.dto.TotalDTO;
import com.workshop.jpa.exception.IncorrectURLFormatException;
import com.workshop.jpa.service.CountryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class CountryController {
  
  @Autowired
  CountryService countryService;
  
  
  @GetMapping("/countries")
  public List<CountryDTO> getCountries(@RequestParam Map<String, String> allParams) {

    log.info("GET /api/countries invoked");
    return countryService.getCountriesWithParams(allParams);

  }
  

  @GetMapping("/countries/count")
  public TotalDTO getCount(@RequestParam Map<String, String> allParams) {

    log.info("GET /api/countries/count invoked");
    return countryService.getCountWithParams(allParams);

  }
  
  
  
  @GetMapping("/countries/{name}")
  public CountryDTO getSingleCountry(@PathVariable String name) {
    
    log.info("GET /api/countries/" + name + " invoked");
    return countryService.getSingleCountry(name);

  }
  
  @GetMapping("/countries/capital/{name}")
  public CountryDTO getSingleCountryByCapital(@PathVariable String name) {
    
    log.info("GET /api/countries/capital/" + name + " invoked");
    return countryService.getSingleCountryByCapital(name);
  }
  
  
  @PostMapping("/countries")
  public void addSingleDeveloper(@RequestBody CountryDTO countryDTO) {
    
    log.info("POST /api/developers invoked");
    countryService.saveCountry(countryDTO);

    /*
     * We do not return a URI in the Location header of the response 
     * in this example, as we assume that the client knows that 
     * the record can be retrieved in the future based on its name
     */
    
  }
  
  @PutMapping("/countries/{name}")
  public void updateCountry(@PathVariable String name, @RequestBody CountryDTO countryDTO) {

    log.info("PUT /api/countries invoked");
    countryService.updateCountry(name, countryDTO);

  }
  
  @DeleteMapping("/countries/{name}")
  public void deleteCountry(@PathVariable String name) {

    log.info("DELETE /api/countries invoked");
    countryService.deleteCountry(name);

  }
  
  
  @PatchMapping(path = "/countries/{name}", consumes = "application/json-patch+json")
  public CountryDTO updateCountry(@PathVariable String name, @RequestBody JsonPatch patch) {
    
    log.info("PATCH /api/countries invoked");
    return countryService.updateCountry(name, patch);

  }

  
  

}
