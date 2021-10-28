package com.workshop.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class HeroService implements IHeroService {
	
	List<Hero> myHeroes;
	
	public HeroService() {
		
		myHeroes = new ArrayList<Hero>();
		
		// create a preconfigured list of heroes 
		// with a single power
		Hero hero1 = new Hero("spiderman");
		hero1.addNewPower("wallcrawling");


		Hero hero2 = new Hero("ironman");
		hero2.addNewPower("powersuit");
		
		Hero hero3 = new Hero("superman");
		hero3.addNewPower("xray vision");

		Hero hero4 = new Hero("wonderwoman");
		hero4.addNewPower("lasso");
		
		myHeroes.add(hero1);
		myHeroes.add(hero2);
		myHeroes.add(hero3);
		myHeroes.add(hero4);
		
	}

	
	@Override
	public List<Hero> getHeroes() {
		return myHeroes;
	}

	@Override
	public void addHeroPowers(List<Hero> existingHeroes) {
		
		for (Hero existingHero : existingHeroes) {
			
			for (Hero myHero : myHeroes) {
				if (myHero.getName().equals(existingHero.getName())) {
					List<String> myPowers = myHero.getPowers();
					myPowers.addAll(existingHero.getPowers());
					myHero.setPowers(myPowers);
				}
				
			}
		}

	}

}
