package com.workshop.mvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import com.workshop.mvc.dto.CountryDTO;
import com.workshop.mvc.dto.TotalDTO;
import com.workshop.mvc.exception.PageListParamException;
import com.workshop.mvc.forms.CountryDetailsForm;
import com.workshop.mvc.forms.GetCountryForm;
import com.workshop.mvc.misc.ServerConstants;
import com.workshop.mvc.service.MyRestService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class FormsController {
  
  @Autowired
  private MyRestService restService;
  
  @GetMapping("/") 
  public String returnMainMenu(Model model) {
    model.addAttribute("jwtstring", restService.getJwtString());
    return "main";
    
  }
  
  @GetMapping("/setjwt")
  public String returnSetJWT(Model model) {
    
    model.addAttribute("jwtstring", restService.getJwtString());
    return "setjwt";
  }
  
  @PostMapping("/setjwt")
  public String processReturnedJWT(@RequestParam("jwt") String jwtString) {
    
    log.info("got the JWT : " + jwtString);
    
    restService.setJwtString(jwtString);
    
    return "redirect:/";
  }
  
  
  @GetMapping("/get") 
  public String returnGetCountryForm(Model model) {
    
    // Set some default values in the GetCountryForm
    GetCountryForm countryForm = new GetCountryForm();
    countryForm.setRetrieveTarget(ServerConstants.NAME_PARAM);
    countryForm.setValToUse("*");
    
    model.addAttribute("countryForm", countryForm);
    
    model.addAttribute("getError", null);
    model.addAttribute("pageLimitError", null);
    model.addAttribute("countryError", null);
    model.addAttribute("operation", null);
    model.addAttribute("serviceError", null); 
    
    return "get";
  
  }


  
  @PostMapping("/get") 
  public String processGetCountryForm(@ModelAttribute("countryForm") GetCountryForm countryForm, Model model) {
    
    log.info("Processing country form : " + countryForm.toString());
    
    List<CountryDTO> countries = null;
    TotalDTO total = null;
    String retrieveTarget = countryForm.getRetrieveTarget();
    String valToUse = countryForm.getValToUse();
    String page = countryForm.getPage();
    String limit = countryForm.getLimit();

    
    if (page.length()>0 || limit.length()>0) {
      
      log.info("Checking page and limit values");
      try {
        restService.setLimitAndPage(limit, page);
      } catch (PageListParamException pe) {
        log.error("Error in processing page and limit params");
        model.addAttribute("pageLimitError", pe.getMessage());
        return "get";
      }

    }
    
    
    if (retrieveTarget.equals(ServerConstants.POPULATION_ASCENDING) || retrieveTarget.equals(ServerConstants.POPULATION_DESCENDING)) {
      
      countries = restService.getCountryList(ServerConstants.SORTING_PARAM, retrieveTarget);
      
    } else {
      
      log.info("Checking value to use");
      
      if (valToUse.length() < 1) {
        model.addAttribute("getError", "You must supply a value for that retrieval action");
        return "get";
      }
          
      if (retrieveTarget.equals(ServerConstants.COUNT_REGION_PARAM)) {

        try {
          total = restService.getTotalCount(ServerConstants.REGION_PARAM, valToUse);
        } catch (HttpClientErrorException hcee) {
          
          log.error("Error message returned from server side");
          model.addAttribute("operation", "serviceError");
          model.addAttribute("serviceError", hcee.getResponseBodyAsString());    
          return "result";
          
        }
        
      } else if (retrieveTarget.equals(ServerConstants.COUNT_LANGUAGE_PARAM)) {
      
        try {
          total = restService.getTotalCount(ServerConstants.LANGUAGE_PARAM, valToUse);
        } catch (HttpClientErrorException hcee) {
          
          log.error("Error message returned from server side");
          model.addAttribute("operation", "serviceError");
          model.addAttribute("serviceError", hcee.getResponseBodyAsString());    
          return "result";
        
        }      
        
      } else {  
        
        try {
          countries = restService.getCountryList(retrieveTarget, valToUse);
        } catch (HttpClientErrorException hcee) {
          
          log.error("Error message returned from server side");
          model.addAttribute("operation", "serviceError");
          model.addAttribute("serviceError", hcee.getResponseBodyAsString());    
          return "result";
          
        }
        
      }
    
    }
    
    model.addAttribute("countries", countries);
    model.addAttribute("total", total);
    return "countries";

  }
  
  
  @GetMapping("/add") 
  public String returnCountryDetailsForm(CountryDetailsForm countryDetailsForm, Model model) {
    
    model.addAttribute("operation", null);
    model.addAttribute("serviceError", null);
    return "add";
  
  }
  

  @PostMapping("/add") 
  public String processCountryDetailsForm(@Valid CountryDetailsForm countryDetailsForm, BindingResult bindingResult, Model model) {
    
    if (bindingResult.hasErrors()) {
      log.info(bindingResult.toString());
      return "add";
    }
    try {
      restService.postCountry(new CountryDTO(countryDetailsForm));
      model.addAttribute("operation", "addSuccess" );
      model.addAttribute("serviceError", "");
    } catch (RuntimeException e) {
      log.error("Error in processing add request for : " + countryDetailsForm);
      model.addAttribute("operation", "serviceError" );
      model.addAttribute("serviceError", e.getMessage());
    }
    
    return "result";
    
  }
  
  
  @GetMapping("/delete") 
  public String selectCountryToDelete(Model model) {
    model.addAttribute("countryError", null);
    model.addAttribute("operation", null);
    model.addAttribute("serviceError", null);    
    return "delete";
  }
  
  @PostMapping("/delete") 
  public String processCountryToDelete(@RequestParam("country") String country, Model model) {

    if (country.trim().length() < 1) {
      model.addAttribute("countryError", "Country name must be specified");
      return "delete";
    }
    
    try {
      
      restService.deleteCountry(country.trim());
      model.addAttribute("operation", "deleteSuccess" );
      model.addAttribute("serviceError", null);

    } catch (HttpClientErrorException hcee) {

      log.info ("Error message returned from server side");
      model.addAttribute("operation", "serviceError");
      model.addAttribute("serviceError", hcee.getResponseBodyAsString());
      
    }
    

    return "result";
  }
  
  
  @GetMapping("/update") 
  public String selectCountryToUpdate(Model model) {
    model.addAttribute("countryError", null);
    model.addAttribute("operation", null);
    model.addAttribute("serviceError", null);    
    return "update";
  }
  
  @PostMapping("/update") 
  public String processCountryToUpdate(@RequestParam("country") String country, Model model) {
    
    
    List<CountryDTO> countries = null;
    
    if (country.trim().length() < 1) {
      model.addAttribute("countryError", "Country name must be specified");
      return "update";
    }
    
    CountryDetailsForm countryForm;
    
    try {
      countries = restService.getCountryList(ServerConstants.NAME_PARAM, country.trim());
      countryForm = new CountryDetailsForm(countries.get(0));
      model.addAttribute("countryDetailsForm",countryForm);
      return "modify";

    } catch (HttpClientErrorException hcee) {
      
      log.info ("Error message returned from server side");
      model.addAttribute("operation", "serviceError");
      model.addAttribute("serviceError", hcee.getResponseBodyAsString());
      return "result";
    }    
    
  }

  
  @PostMapping("/modify")
  public String processModifyCountryDetailsForm(@Valid CountryDetailsForm countryDetailsForm, BindingResult bindingResult, Model model) {
    
    if (bindingResult.hasErrors()) {
      log.info(bindingResult.toString());
      return "modify";
    }
    try {
      restService.putCountry(new CountryDTO(countryDetailsForm));
      model.addAttribute("operation", "modifySuccess" );
      model.addAttribute("serviceError", null);
    } catch (HttpClientErrorException hcee) {
      log.error("Error in processing PUT request for : " + countryDetailsForm);
      model.addAttribute("operation", "serviceError" );
      model.addAttribute("serviceError", hcee.getResponseBodyAsString());
    }
    
    return "result";
    
  }
  


}
