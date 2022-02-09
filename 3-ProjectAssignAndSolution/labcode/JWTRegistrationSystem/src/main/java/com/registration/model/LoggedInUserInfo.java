package com.registration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoggedInUserInfo {
  
  public String email;
  public boolean oauthLogin;
  public boolean firstTimeLogin;
  public RegUser currentUser;
  

}
