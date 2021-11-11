package com.workshop.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponse {
	
    private String message;
    private String status;
    private String jwt;
    
    
    public JwtResponse(String jwt) {
    	this ("API Response","SUCCESS",jwt);
    }
}
