package com.workshop.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@ToString
public class CustomUserDetails {

	private String userName;
	private String realUserName;
	private String userEmail;
	private String userLocation;
	private String userCompany;
	
	
}
