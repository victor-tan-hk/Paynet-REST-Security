package com.workshop.mvc.forms;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.workshop.mvc.dto.CountryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CountryDetailsForm {
  
  @NotEmpty(message = "Name must be provided")
  private String name;
  
  @NotEmpty(message = "Capital must be provided")
  private String capital;

  @NotEmpty(message = "Region must be provided")
  private String region;
  
  @NotEmpty(message = "Subregion must be provided")
  private String subregion;
  
  @NotNull
  @Min(value = 0)
  private Long population;
  
  @NotEmpty(message = "Currencies must be provided")
  private String currencies;
  
  @NotEmpty(message = "Languages must be provided")
  private String languages;
  
  
  public CountryDetailsForm(CountryDTO cdto) {
    this.name = cdto.getName();
    this.capital = cdto.getCapital();
    this.region = cdto.getRegion();
    this.subregion = cdto.getSubregion();
    this.population = cdto.getPopulation();
    
    String currencyToStore = "";
    for (String curr : cdto.getCurrencies()) {
      currencyToStore += curr + ",";
    }
    if (currencyToStore.length() > 1)
      this.currencies = currencyToStore.substring(0,currencyToStore.length()-1);
    else
      this.currencies = "";
    
    String langToStore = "";
    for (String lang : cdto.getLanguages()) {
      langToStore += lang + ",";
    }
    if (langToStore.length() > 1)
      this.languages = langToStore.substring(0,langToStore.length()-1);
    else
      this.languages = "";
  }


 
}
