package com.workshop.mvc.dto;

import lombok.Data;

@Data
public class PatchFormatDTO {
  
  private String op;
  private String path;
  private String value;
  

}
