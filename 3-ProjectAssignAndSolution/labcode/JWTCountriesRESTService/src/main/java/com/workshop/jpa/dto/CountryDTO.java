package com.workshop.jpa.dto;

import com.workshop.jpa.model.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CountryDTO {
  
  private Integer id;
  private String name;
  private String capital;
  private String region;
  private String subregion;
  private Long population;
  private String[] currencies;
  private String[] languages;
  
  
  public CountryDTO(Country country) {
    this.id = country.getId();
    this.name = country.getName();
    this.capital = country.getCapital();
    this.region = country.getRegion();
    this.subregion = country.getSubregion();
    this.population = country.getPopulation();
    if (country.getCurrencies().length() > 1)
      this.currencies = country.getCurrencies().split(",");
    else
      this.currencies = new String[0];
    
    if (country.getLanguages().length() > 1)
      this.languages = country.getLanguages().split(",");
    else
      this.languages = new String[0];
    
  }

}
