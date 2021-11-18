package com.workshop.security;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomErrorMessage {
  private Date timestamp;
  private int status;
  private String error;
  private String message;
  private int internalErrorId;
  private String infoURL;
  

}
