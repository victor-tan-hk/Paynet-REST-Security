package com.workshop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class MySigningKeyResolver extends SigningKeyResolverAdapter {

	private Map<String, Key> keyMap = new HashMap<>();
	
	public void addKeyMapping(SignatureAlgorithm algoId, Key keyToAdd) {
		keyMap.put(algoId.getValue(), keyToAdd);
	}

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
    	log.info("Inside resolver : Jwsheader : " + jwsHeader.getAlgorithm());
    	return keyMap.get(jwsHeader.getAlgorithm());
    }

}
