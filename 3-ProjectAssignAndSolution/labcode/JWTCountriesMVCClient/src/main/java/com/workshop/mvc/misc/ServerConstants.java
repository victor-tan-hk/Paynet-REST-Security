package com.workshop.mvc.misc;

// This collects together all the constants that are used by various other classes
// in the application - instead of placing them into application.properties
public class ServerConstants {
  

  // Constants for query parameters using in GET queries
  public static final String NAME_PARAM = "name";
  
  public static final String INCLUDE_ALL_PARAM = "*";
  
  public static final String CAPITAL_PARAM = "capital";
  public static final String LANGUAGE_PARAM = "language";
  public static final String LANGUAGE_NUMBER_PARAM = "langnum";
  public static final String REGION_PARAM = "region";
  public static final String SUBREGION_PARAM = "subregion";
  public static final String CURRENCY_PARAM = "currency";

  public static final String COUNT_REGION_PARAM = "count-region";
  public static final String COUNT_LANGUAGE_PARAM = "count-language";
  
  public static final String POPULATION_PARAM = "pop";
  public static final String POPULATION_MORE_PARAM = "popmore";
  public static final String POPULATION_LESS_PARAM = "popless";
  
  public static final String SORTING_PARAM = "sort";
  public static final String POPULATION_ASCENDING = "pop-asc";
  public static final String POPULATION_DESCENDING = "pop-desc";

  public static final String PAGE_PARAM = "page";
  public static final String LIMIT_PARAM = "limit";

}
