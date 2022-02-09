package com.workshop.jpa.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.workshop.jpa.ServerConstants;
import com.workshop.jpa.dto.CountryDTO;
import com.workshop.jpa.dto.TotalDTO;
import com.workshop.jpa.exception.CountryNotFoundException;
import com.workshop.jpa.exception.IncorrectJSONFormatException;
import com.workshop.jpa.exception.IncorrectURLFormatException;
import com.workshop.jpa.model.Country;
import com.workshop.jpa.repository.CountryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CountryService {

  @Autowired
  private CountryRepository countryRepo;

  @Autowired
  private RestTemplate myRestTemplate;

  @Value("${rapidapi.baseurl}")
  private String rapidAPIBaseUrl;

  @Value("${rapidapi.host}")
  private String rapidAPIHost;

  @Value("${rapidapi.key}")
  private String rapidAPIKey;
  
  @Value("${json.file}")
  private String jsonFile;


  
  public CountryDTO getSingleCountry(String name) {

    List<Country> countryList = countryRepo.findByName(name);
    if (countryList.size() == 0)
      throw new CountryNotFoundException("Country with name : " + name + " does not exist !");
    List<CountryDTO> countryDTOList = convertCountryListToCountryDTOList(countryList);
    // We expect only one country with a given name, since names are unique
    // so there should only be one item in the list.
    return countryDTOList.get(0);

  }

  public CountryDTO getSingleCountryByCapital(String name) {

    List<Country> countryList = countryRepo.findByCapital(name);
    if (countryList.size() == 0)
      throw new CountryNotFoundException("Country with capital : " + name + " does not exist !");
    List<CountryDTO> countryDTOList = convertCountryListToCountryDTOList(countryList);
    // We expect only one country with a given capital, since capitals are unique
    // so there should only be one item in the list.
    return countryDTOList.get(0);

  }
  
  
  public void saveCountry(CountryDTO countryDTO) {
    
    if (isCountryValid(countryDTO)) {
      Country newCountry = new Country(countryDTO);
      if (countryRepo.existsByName(newCountry.getName()))
        throw new IncorrectURLFormatException("Country with name : " + newCountry.getName() + " already exists");

      log.info("Saving new country : " + newCountry.toString());
      countryRepo.save(newCountry);
    }
    else 
      throw new IncorrectJSONFormatException("JSON is valid, but incorrect format for country");
    
  }
  
  // Update for a PUT method call
  public void updateCountry(String name, CountryDTO countryDTO) {

    if (isCountryValid(countryDTO)) {

      Country newCountry = new Country(countryDTO);

      if (!countryRepo.existsByName(name))
        throw new IncorrectURLFormatException("Country with name : " + name + " does not exist");

      log.info("Updating country : " + name);
      log.info("With new contents : " + newCountry.toString());

      countryRepo.updateExistingRecord(newCountry.getName(), newCountry.getCapital(), newCountry.getRegion(), newCountry.getSubregion(), newCountry.getPopulation(), newCountry.getCurrencies(), newCountry.getLanguages());

    }
    else 
      throw new IncorrectJSONFormatException("JSON is valid, but incorrect format for country");
  }
  
  
  // Update for a PATCH method call
  public CountryDTO updateCountry(String name, JsonPatch patch) {
    
    CountryDTO countryToUpdate = getSingleCountry(name);
    ObjectMapper objectMapper = new ObjectMapper();
    
    try {
      JsonNode patched = patch.apply(objectMapper.convertValue(countryToUpdate, JsonNode.class));
      CountryDTO updatedCountry = objectMapper.treeToValue(patched, CountryDTO.class);
      log.info("Updated Country : " + updatedCountry.toString());
      updateCountry(name, updatedCountry);
      return updatedCountry;
      
    } catch (IllegalArgumentException | JsonPatchException e) {
      log.info(e.getMessage());
      throw new IncorrectJSONFormatException(e.getMessage());
    } catch (JsonProcessingException e) {
      log.info(e.getMessage());
      throw new IncorrectJSONFormatException(e.getMessage());
    }
    
  }
  
  
  public void deleteCountry(String name) {
    
    if (!countryRepo.existsByName(name))
      throw new IncorrectURLFormatException("Country with name : " + name + " does not exist");

    log.info("Deleting country : " + name);
    List<Country> countries = countryRepo.findByName(name);
    countryRepo.deleteById(countries.get(0).getId());
    
  }
  
  
  // Handle the processing of all possible query parameters in the GET method call
  // This will return a list of countries
  public List<CountryDTO> getCountriesWithParams(Map<String, String> allParams) {

    String regionVal = null;
    String subregionVal = null;
    String langVal = null;
    String currVal = null;
    String sortType = null;

    int langNumVal = 0;

    long greaterThanPop = -1;
    long lesserThanPop = -1;

    // By default, set page and limit values
    // to return a complete result set
    int pageVal = 0;
    int limitVal = 10000;
    
    boolean foundMatchingParam = false;
    
    // Iterate through all the query parameters and process accordingly
    for (Map.Entry<String, String> entry : allParams.entrySet()) {

      String tempKey = entry.getKey();
      String tempValue = entry.getValue();
      log.info(tempKey + " : " + tempValue);

      if (tempKey.equals(ServerConstants.REGION_PARAM)) {

        foundMatchingParam = true;
        regionVal = tempValue;

      } else if (tempKey.equals(ServerConstants.SUBREGION_PARAM)) {

        foundMatchingParam = true;
        subregionVal = tempValue;

      } else if (tempKey.equals(ServerConstants.LANGUAGE_PARAM)) {

        foundMatchingParam = true;
        langVal = tempValue;

      } else if (tempKey.equals(ServerConstants.CURRENCY_PARAM)) {

        foundMatchingParam = true;
        currVal = tempValue;

      } else if (tempKey.equals(ServerConstants.SORTING_PARAM)) {

        foundMatchingParam = true;
        sortType = tempValue;

      } else if (tempKey.equals(ServerConstants.LANGUAGE_NUMBER_PARAM)) {

        foundMatchingParam = true;

        try {
          langNumVal = Integer.parseInt(tempValue);
        } catch (NumberFormatException ex) {
          throw new IncorrectURLFormatException("langnum parameter must have numeric value");
        }

      } else if (tempKey.contains(ServerConstants.POPULATION_PARAM)) {

        foundMatchingParam = true;

        String errMessage = "population parameter is either specified as pop>X or pop<Y, where X and Y are positive numbers";
        String[] strArray;

        if (tempKey.contains(">")) {

          strArray = tempKey.split(">");
          try {
            greaterThanPop = Integer.parseInt(strArray[1]);
          } catch (NumberFormatException ex) {
            throw new IncorrectURLFormatException(errMessage);
          }

        } else if (tempKey.contains("<")) {

          strArray = tempKey.split("<");
          try {
            lesserThanPop = Integer.parseInt(strArray[1]);
          } catch (NumberFormatException ex) {
            throw new IncorrectURLFormatException(errMessage);
          }
        }

        if (greaterThanPop <= 0 && lesserThanPop <= 0)
          throw new IncorrectURLFormatException(errMessage);

      } else if (tempKey.equals(ServerConstants.LIMIT_PARAM)) {
        
        foundMatchingParam = true;

        try {
          limitVal = Integer.parseInt(tempValue);
        } catch (NumberFormatException ex) {
          throw new IncorrectURLFormatException("limit parameter must be a number");
        }
        if (limitVal < 0)
          throw new IncorrectURLFormatException("limit parameter must be a positive number");

      } else if (tempKey.equals(ServerConstants.PAGE_PARAM)) {

        foundMatchingParam = true;

        try {
          pageVal = Integer.parseInt(tempValue);
        } catch (NumberFormatException ex) {
          throw new IncorrectURLFormatException("page parameter must be a number");
        }
        if (pageVal < 0)
          throw new IncorrectURLFormatException("page parameter must be a positive number");

      }
    }
    
    // If there are query parameters supplied and none of them are valid parameters
    // throw an exception: meaning we will still process as long as there is 
    // at least one valid parameter
    if ((allParams.size() > 0) && !foundMatchingParam)
      throw new IncorrectURLFormatException("Unrecognized query parameter in URL");
    
    // Create a pageable object to be used with all results returned
    Pageable paging = PageRequest.of(pageVal, limitVal);

    // Create an empty list that will later be initialized with the 
    // result set to  be returned to client
    List<CountryDTO> listToReturn = new ArrayList<CountryDTO>();

    
    // Based on variable values initialized during processing of query parameters
    // execute the appropriate action on the repository

    if (regionVal != null) {
      
      log.info("Returning all countries in region : " + regionVal);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findByRegion(regionVal,paging));

    } else if (subregionVal != null) {

      log.info("Returning all countries in subregion : " + subregionVal);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findBySubregion(subregionVal,paging));

    } else if (langVal != null) {
      
      log.info("Returning all countries that use language : " + langVal);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findByLanguagesContaining(langVal,paging));

    } else if (currVal != null) {
      
      log.info("Returning all countries that use currency : " + currVal);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findByCurrenciesContaining(currVal,paging));

    } else if (langNumVal > 0) {

      log.info("Returning all countries that use " + langNumVal + " languages");
      // Only return countries whose number of languages
      // given by the length of the languages array
      // is equal to langNumVal
      Iterable<Country> countryList = countryRepo.findAll();
      for (Country country : countryList) {
        CountryDTO newCountry = new CountryDTO(country);
        if (newCountry.getLanguages().length == langNumVal)
          listToReturn.add(newCountry);
      }
      
      /*
       * FOR FUTURE WORK:
       * Since we are doing our own business logic here rather than calling a
       * repository method directly we cannot apply the pagination facility of the
       * Spring repository.
       * Either we re implement this business logic into a respository method call 
       * or we implement our own pagination logic here as well
       */

    } else if (lesserThanPop > 0) {
      
      
      log.info("Returning all countries with a smaller population than : " + lesserThanPop);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findByPopulationLessThan(lesserThanPop, paging));

    } else if (greaterThanPop > 0) {
      
      log.info("Returning all countries with a larger population than : " + greaterThanPop);
      listToReturn = convertCountryListToCountryDTOList(countryRepo.findByPopulationGreaterThan(greaterThanPop,paging));

    } else if (sortType != null) {
      
      if (sortType.equals(ServerConstants.POPULATION_ASCENDING)) {
        
        log.info("Returning all countries sorted on ascending order of population");
        paging = PageRequest.of(pageVal, limitVal, Sort.by("population").ascending());
        listToReturn = convertCountryListToCountryDTOList(countryRepo.findAll(paging));

      } else if (sortType.equals(ServerConstants.POPULATION_DESCENDING)) {

        log.info("Returning all countries sorted on descending order of population");
        paging = PageRequest.of(pageVal, limitVal, Sort.by("population").descending());
        listToReturn = convertCountryListToCountryDTOList(countryRepo.findAll(paging));

      } else
        throw new IncorrectURLFormatException("sort parameter must be either " + ServerConstants.POPULATION_ASCENDING
            + " or " + ServerConstants.POPULATION_DESCENDING);

    } else {

      // If no query parameters other than page or limit supplied
      // then return all countries

      log.info("Returning all countries");
      Iterable<Country> countryList = countryRepo.findAll(paging);
      listToReturn = convertCountryListToCountryDTOList(countryList);

    }
    

    return listToReturn;

  }
  
  // Handle the processing of all possible query parameters in the GET method call
  // This will return a count total
  public TotalDTO getCountWithParams(Map<String, String> allParams) {

    String regionVal = null;
    String langVal = null;

    TotalDTO totalToReturn = new TotalDTO();

    for (Map.Entry<String, String> entry : allParams.entrySet()) {

      String tempKey = entry.getKey();
      String tempValue = entry.getValue();
      log.info(tempKey + " : " + tempValue);

      if (tempKey.equals(ServerConstants.REGION_PARAM)) {

        regionVal = tempValue;

      } else if (tempKey.equals(ServerConstants.LANGUAGE_PARAM)) {

        langVal = tempValue;

      }

      if (langVal != null) {

        totalToReturn.setColname(ServerConstants.LANGUAGE_PARAM);
        totalToReturn.setColvalue(langVal);
        totalToReturn.setTotal(countryRepo.countCountriesUsingLanguage(langVal));

      } else if (regionVal != null) {

        totalToReturn.setColname(ServerConstants.REGION_PARAM);
        totalToReturn.setColvalue(regionVal);
        totalToReturn.setTotal(countryRepo.countCountriesInRegion(regionVal));

      }

    }

    return totalToReturn;
  }

  // This initializes the database table
  // by making a call to the public REST API
  public void initializeFromPublicAPI() {

    log.info("Initializing database table countries from : " + rapidAPIBaseUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.set(ServerConstants.RAPIDAPI_HOST_HEADER, rapidAPIHost);
    headers.set(ServerConstants.RAPIDAPI_KEY_HEADER, rapidAPIKey);
    HttpEntity<String> requestEntity = new HttpEntity<String>("body", headers);

    ResponseEntity<List<CountryDTO>> countryResponse = myRestTemplate.exchange(rapidAPIBaseUrl + "/all", HttpMethod.GET,
        requestEntity, new ParameterizedTypeReference<List<CountryDTO>>() {
        });

    List<CountryDTO> countryDTOs = countryResponse.getBody();
    for (CountryDTO cdto : countryDTOs) {
      countryRepo.save(new Country(cdto));
    }
    log.info("Database table countries initialized with : " + countryDTOs.size() + " records");

  }
  
  public void initializeFromJSONFile() {

    log.info("Initializing from : " + jsonFile);

    String fileContent = "";
    try {
      fileContent = new String(Files.readAllBytes(Paths.get("src/main/resources/" + jsonFile)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode, countryArray;
    List<String> currenciesList;
    List<String> languagesList = new ArrayList<String>();

    try {
      rootNode = mapper.readValue(fileContent, JsonNode.class);
      countryArray = rootNode.get("country");
      
      for (JsonNode tempNode : countryArray) {
        
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(tempNode.get("name").asText());
        countryDTO.setCapital(tempNode.get("capital").asText());
        countryDTO.setRegion(tempNode.get("region").asText());
        countryDTO.setSubregion(tempNode.get("subregion").asText());
        countryDTO.setPopulation(tempNode.get("population").asLong(0));

        currenciesList = new ArrayList<String>();
        for (JsonNode currNode : tempNode.get("currencies")) {
          currenciesList.add(currNode.asText());
        }
        countryDTO.setCurrencies(currenciesList.toArray(new String[0]));
        
        languagesList = new ArrayList<String>();
        for (JsonNode langNode : tempNode.get("languages")) {
          languagesList.add(langNode.asText());
        }
        countryDTO.setLanguages(languagesList.toArray(new String[0]));
        
        log.info(countryDTO.toString());
        countryRepo.save(new Country(countryDTO));

      }

    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  
  // Private method to perform entity to DTO conversion
  private List<CountryDTO> convertCountryListToCountryDTOList(Iterable<Country> countryList) {

    List<CountryDTO> myCountries = new ArrayList<CountryDTO>();
    for (Country country : countryList) {
      myCountries.add(new CountryDTO(country));
    }

    return myCountries;

  }
  
  // Use to ensure that a DTO object has all its fields
  // initialized properly before use
  private boolean isCountryValid(CountryDTO country) {
    
    return (country.getName() != null &&
        country.getCapital() != null &&
        country.getRegion() != null &&
        country.getSubregion() != null &&
        country.getPopulation() >= 0 &&
        country.getCurrencies() != null &&
        country.getLanguages() != null);
    
  }

  
  
  
}
