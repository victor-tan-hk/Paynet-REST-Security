package com.workshop.mvc.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class GetCountryForm {

  String retrieveTarget;
  String sortType;
  String valToUse;
  String page;
  String limit;
  
}
