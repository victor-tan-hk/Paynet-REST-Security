package com.workshop.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Developer {
  
  private Integer id;
  private String name;
  private Integer age;
  private String[] languages;
  private boolean married;
  
  
}
