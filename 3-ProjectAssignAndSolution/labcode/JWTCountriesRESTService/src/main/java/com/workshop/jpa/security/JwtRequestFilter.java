package com.workshop.jpa.security;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.workshop.jpa.ServerConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  JWTServiceProvider jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");
    log.info("The request token header is : " + requestTokenHeader);

    /*
     * We set various values for the subject and role which can be used by the
     * CustomAccessDeniedHandler to determine the specific exception message to
     * return to the user. We initially set it to a value indicating undetermining
     * error
     */
    String subject = ServerConstants.DUMMY_NAME;
    String role = ServerConstants.PARSING_ERROR_JWS;
    
    // JWT Token is in the form "Bearer token". Remove Bearer word and retrieve the token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      
      String jwtToken = requestTokenHeader.substring(7);
      log.info("jwtToken received is : " + jwtToken);
      
      try {
        Claims claims = jwtService.getAllClaimsFromToken(jwtToken, jwtService.getSecretKey());
        subject = claims.getSubject();
        role = claims.get("role", String.class);
        log.info("Subject : " + subject);
        log.info("Role : " + role);
        log.info("Issuer : " + claims.getIssuer());
        log.info("Issued at : " + claims.getIssuedAt());
        log.info("Expiration : " + claims.getExpiration());

      } catch (SignatureException se) {
        
        role = ServerConstants.INVALID_JWS;
        
      } catch (ExpiredJwtException ejwe) {
        
        role = ServerConstants.EXPIRED_JWS;
        
      } catch (Exception e) {
        
      }
      
    }
    else {
      
      role = ServerConstants.MISSING_JWS;
      
    }
    
    
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority(role));
    
    // Can simply use a dummy value for the credentials portion of the following constructors
    // as we no longer need to retrieve the credentials at this point in the filter chain
    User createdUser = new User(subject, "dummypassword", authorities);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        createdUser, "dummy", authorities);
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
 
    // Pass this request on to the authorization portion of the Spring Security framework
    // which will use the role of the newly created usernamePasswordAuthenticationToken
    // to perform authorization
    chain.doFilter(request, response);


  }

}
