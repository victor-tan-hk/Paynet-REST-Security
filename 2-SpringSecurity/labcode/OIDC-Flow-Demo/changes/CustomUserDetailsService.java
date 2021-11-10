package com.workshop.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {
	
	List<CustomUserDetails> userList;
	
	public CustomUserDetailsService() {
		userList = new ArrayList<CustomUserDetails>();
		
		CustomUserDetails userDetails1 = new CustomUserDetails(StringConstantsHolder.user1Name, "Peter Parker", "peter@marveluniverse.com","New York City","Daily Bugle"); 

		CustomUserDetails userDetails2 = new CustomUserDetails(StringConstantsHolder.user2Name, "Tony Stark", "tony@marveluniverse.com","New York City","Stark Industries"); 

		CustomUserDetails userDetails3 = new CustomUserDetails(StringConstantsHolder.user3Name, "Clark Kent", "clark@dccomics.com","Metropolis","Daily Planet"); 

		CustomUserDetails userDetails4 = new CustomUserDetails(StringConstantsHolder.user4Name, "Princess Diana of Themyscira", "diana@dccomics.com","Washington DC","Smithsonian"); 
		
		userList.add(userDetails1);
		userList.add(userDetails2);
		userList.add(userDetails3);
		userList.add(userDetails4);
	}
	
	public String getRealUserName(String userName) {
		for (CustomUserDetails userDetails : userList) {
			if (userDetails.getUserName().equals(userName)) {
				return userDetails.getRealUserName();
			}
		}
		return null;
	}
	
	public String getUserEmail(String userName) {
		for (CustomUserDetails userDetails : userList) {
			if (userDetails.getUserName().equals(userName)) {
				return userDetails.getUserEmail();
			}
		}
		return null;
	}
	
	public String getUserLocation(String userName) {
		for (CustomUserDetails userDetails : userList) {
			if (userDetails.getUserName().equals(userName)) {
				return userDetails.getUserLocation();
			}
		}
		return null;
	}
	
	public String getUserCompany(String userName) {
		for (CustomUserDetails userDetails : userList) {
			if (userDetails.getUserName().equals(userName)) {
				return userDetails.getUserCompany();
			}
		}
		return null;
	}	

}
