package com.workshop.basics;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Developer implements Serializable {
  
  private String name;
  private int age;
  private boolean married;

}
