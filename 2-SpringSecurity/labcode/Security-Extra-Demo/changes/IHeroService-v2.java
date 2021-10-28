package com.workshop.security;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

public interface IHeroService {
	
	@PostFilter ("hasRole('ROLE_ADMIN')")
	public List<Hero> getHeroes();
	
	@PreFilter("hasRole('ROLE_ADMIN')")
	public void addHeroPowers(List<Hero> existingHeroes);

}



