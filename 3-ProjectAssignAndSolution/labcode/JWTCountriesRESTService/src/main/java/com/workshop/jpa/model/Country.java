package com.workshop.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.workshop.jpa.dto.CountryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;
    private String capital;
    private String region;
    private String subregion;
    private Long population;
    private String currencies;
    private String languages;
    
    
    public Country(CountryDTO cdto) {
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