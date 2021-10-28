package com.workshop.security;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Hero {
	
	private String name;
	private List<String> powers;
	
	public void addNewPower(String power) {
		powers.add(power);
	}
	
	public Hero(String name) {
		this.name = name;
		powers = new ArrayList<String>();
	}
	

}
