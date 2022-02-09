package com.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name="regusers")
public class RegUser {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
 
  // relevant info for authentication purposes
  // directly related to UserDetails
  
  // We use the user's email as their username
  // for login purposes, rather than ask them to provide 
  // a "standard" username, since we working with social logins
  // and the single reliable UUID to differentiate
  // users from OIDC / OAuth2 authentication 
  // is their email address.
  
  @Column(nullable=false, unique = true)
  private String email;

  private String password;
  
//  this is used to designate role for accessing resources
//  on the current app
  private String roleMVC;
  
//to enable or disable account
  private boolean enabled; 

  
  // additional info related to authentication
  // not related to UserDetails 
  private String firstName;
  private String lastName;
  
//this is used to designate role for user that will be issued 
//on the JWT and used to access the secured REST service
  private String roleREST;
  
  
}

