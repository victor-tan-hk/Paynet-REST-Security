package com.workshop.jpa.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.workshop.jpa.model.Country;

public interface CountryRepository extends PagingAndSortingRepository<Country, Integer> {
  
  List<Country> findByName(String name);
  
  List<Country> findByCapital(String name);

  List<Country> findByRegion(String region, Pageable paging);

  List<Country> findBySubregion(String subregion, Pageable paging);
  
  // Find all countries that can speak in a specified language
  List<Country> findByLanguagesContaining(String langToFind, Pageable paging);
  
  // Find all countries that use specified currency
  List<Country> findByCurrenciesContaining(String currToFind, Pageable paging);
  
  // Count the number of countries that use a specified language
  @Query(value = "SELECT COUNT(*) FROM countries WHERE INSTR(languages,:langToFind) > 0", 
      nativeQuery = true)
  int countCountriesUsingLanguage(@Param("langToFind") String langToFind);
  
  // Count the number of countries in a specified region
  @Query(value = "SELECT COUNT(*) FROM countries WHERE INSTR(region,:regionToFind) > 0", 
      nativeQuery = true)
  int countCountriesInRegion(@Param("regionToFind") String regionToFind);
  
  // Find all countries with a population less than a specified number
  List<Country> findByPopulationLessThan(Long age, Pageable paging);

  // Find all countries with a population greater than a specified number
  List<Country> findByPopulationGreaterThan(Long age, Pageable paging);
  
  // Check whether a specific country with a given name is in the table
  boolean existsByName(String name);
  
  // Update existing row in the table by its name
  @Modifying
  @Transactional
  @Query(value = "UPDATE countries SET capital = :countryCapital, region = :countryRegion, subregion = :countrySubregion, population = :countryPopulation, currencies = :countryCurrencies, languages = :countryLanguages WHERE name = :countryName", 
      nativeQuery = true)
  
  void updateExistingRecord(@Param("countryName") String countryName, @Param("countryCapital") String countryCapital, @Param("countryRegion") String countryRegion, @Param("countrySubregion") String countrySubregion, @Param("countryPopulation") Long countryPopulation, @Param("countryCurrencies") String countryCurrencies, @Param("countryLanguages") String countryLanguages);

  
}
