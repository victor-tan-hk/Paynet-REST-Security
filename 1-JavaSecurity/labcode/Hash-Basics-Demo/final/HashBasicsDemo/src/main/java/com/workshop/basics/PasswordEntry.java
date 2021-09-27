package com.workshop.basics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PasswordEntry {
  
  private String userName;
  private int iterations; // Number of iterations for the PKBDF2 algorithm
  private String salt; // Salt as either a HEX or Base64 string
  private String hash; // Final hash as either a HEX or Base64 string

}
