package com.workshop.jpa;

// This collects together all the constants that are used by various other classes
// in the application - instead of placing them into application.properties
public class ServerConstants {
  
  // Constants for deciding on how to initialize the database table
  public static final String READ_FILE_OPTION = "file";
  public static final String RETRIEVE_SERVER_OPTION = "server";
  
  
  // Constants for setting headers to interact with the RapidAPI service
  public static final String RAPIDAPI_HOST_HEADER = "x-rapidapi-host";
  public static final String RAPIDAPI_KEY_HEADER = "x-rapidapi-key";

  // Constants for query parameters using in GET queries
  public static final String LANGUAGE_PARAM = "language";
  public static final String LANGUAGE_NUMBER_PARAM = "langnum";
  public static final String REGION_PARAM = "region";
  public static final String SUBREGION_PARAM = "subregion";
  public static final String CURRENCY_PARAM = "currency";
  public static final String POPULATION_PARAM = "pop";
  public static final String SORTING_PARAM = "sort";
  public static final String POPULATION_ASCENDING = "pop-asc";
  public static final String POPULATION_DESCENDING = "pop-desc";

  public static final String PAGE_PARAM = "page";
  public static final String LIMIT_PARAM = "limit";

  //  Constants for working with JWS validation

  public static final String MISSING_JWS = "missingjws";
  public static final String EXPIRED_JWS = "expiredjws";
  public static final String INVALID_JWS = "invalidjws";
  public static final String PARSING_ERROR_JWS = "parsingerrorjws";
  
  public static final String DUMMY_NAME = "dummyname";

  
}
