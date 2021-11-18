package com.workshop.security;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenUtil {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	Key keyToUse = null;

	public void setKeyToUse(Key keyToUse) {
		this.keyToUse = keyToUse;
	}

	public Claims getAllClaimsFromToken(String token) {

		Claims claims = null;
		try {

			JwtParser parser = Jwts.parserBuilder().setSigningKey(keyToUse).build();
			claims = parser.parseClaimsJws(token).getBody();

		} catch (JwtException e) {

			log.info("Error in signature validation");
			log.info(e.getMessage());

		}
		return claims;

	}



	public String generateToken(Map<String, Object> claims, String subject, String issuer) {

		Date now = new Date(System.currentTimeMillis());

		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer).addClaims(claims)
				.signWith(keyToUse);

		String jwtString = builder.compact();
		return jwtString;

	}


}