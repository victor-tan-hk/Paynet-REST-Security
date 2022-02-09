package com.workshop.jpa.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.jpa.ServerConstants;
import com.workshop.jpa.dto.CustomErrorMessage;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    String messageToReturn = "";
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   
    if (auth != null) {

      User user = (User) auth.getPrincipal();
      Collection<GrantedAuthority> authorities = user.getAuthorities();
      GrantedAuthority firstAuthority = authorities.iterator().next();
      String role = firstAuthority.getAuthority();
      
      /*
       * The error message content to be returned to the user
       * depends on the specific content of the role field set earlier
       * in JwtRequestFilter
       */
      if (role.contentEquals(ServerConstants.MISSING_JWS)) 
        messageToReturn = "You need to obtain a valid JWS with a designated role and submit in Authorization: Bearer of your request";
      else if (role.contentEquals(ServerConstants.INVALID_JWS)) 
        messageToReturn = "The signature on the submitted JWS is not valid";
      else if (role.contentEquals(ServerConstants.EXPIRED_JWS))
        messageToReturn = "The JWS expiration date has been reached";
      else {
        messageToReturn = "Submitted JWS has role " + role;
        messageToReturn += " which is not authorized to access : " + request.getRequestURI() + " via a "
            + request.getMethod();
      }
    }
    else {
      messageToReturn = "Unexpected error in determining the role of the submitted JWS";
    }

    HttpStatus statusToReturn = HttpStatus.FORBIDDEN;

    // Additional specialized info to return
    int localErrorId = 666;
    String urlToConsult = "https://marveluniverse.com.my/error-troubleshooting";

    CustomErrorMessage newMessage = new CustomErrorMessage(new Date(), statusToReturn.value(),
        statusToReturn.toString(), messageToReturn, localErrorId, urlToConsult);

    ObjectMapper mapper = new ObjectMapper();
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(403);
    response.getWriter().write(mapper.writeValueAsString(newMessage));
  }
}