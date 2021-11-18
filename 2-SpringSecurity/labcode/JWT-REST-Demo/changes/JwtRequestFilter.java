package com.workshop.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		log.info("JWT Request Filter invoked");

		final String requestTokenHeader = request.getHeader("Authorization");

		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

			String jwtToken = requestTokenHeader.substring(7);

			Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);

			String userName = "dummy";
			String role = "dummy";

			if (claims != null) {

				userName = claims.getSubject();
				role = claims.get("role", String.class);

				log.info("Subject : " + userName);
				log.info("Issuer : " + claims.getIssuer());
				log.info("Issued at : " + claims.getIssuedAt());
				log.info("Role : " + role);
			}

			Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(role));

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userName, "dummy", authorities);

			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			if (claims == null) {
				usernamePasswordAuthenticationToken.setAuthenticated(false);
			}

			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		} else {
			log.info("The request does not include an appropriate JWT token in the Authorization: Bearer");
		}

		chain.doFilter(request, response);
	}

}