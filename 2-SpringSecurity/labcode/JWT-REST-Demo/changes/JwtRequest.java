package com.workshop.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
 
  private String username;
  private String password;

}