package com.workshop.security;

import java.util.Date;

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
@Table(name="users")
public class AppUser {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
 
  // relevant info for authentication purposes
  // directly related to UserDetails
  @Column(nullable=false, unique = true)
  private String username;
  private String password;
  private String authority;
  private boolean enabled; // to enable or disable account

  // additional info related to authentication
  // not related to UserDetails 
  private Date lastLogin;
  
/*
 * personal info not related to authentication this 
 * could  alternatively be modeled via a separate 
 * @Entity class and linked via a one-to-one relationship 
 * to this class
 */
  private String realname;
  private Integer age;
  
}
