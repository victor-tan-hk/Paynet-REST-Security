package com.registration.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.DefaultPropertiesPersister;

import com.registration.forms.ChangeDateForm;
import com.registration.forms.RegUserForm;
import com.registration.forms.UpdateUserForm;
import com.registration.model.LoggedInUserInfo;
import com.registration.model.RegUser;
import com.registration.repository.RegUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegistrationService {
  
  static final String JwtPropertiesFileName = "src/main/resources/jwt.properties";
	
	@Autowired
	RegUserRepository userRepo;
	
  @Autowired
  JWTServiceProvider jwtService;

	PasswordEncoder pwe = new BCryptPasswordEncoder();
	
	@Value("${exp.days}")
	int expDays;
	
	@Value("${exp.hours}")
	int expHours;
	
  @Value("${exp.minutes}")
  int expMinutes;	
	
	
//	Create a new user account in repo with default setting for roleMVC and roleREST
	public void createNewUserAccount(RegUserForm userForm) {
		
		RegUser usertoSave = new RegUser(0L, userForm.getEmail(), pwe.encode(userForm.getPassword()), "ROLE_USER", true,
				userForm.getFirstName(), userForm.getLastName(), "ROLE_BASIC");
		userRepo.save(usertoSave);
		
	}
	
	public boolean checkEmailExistsInRepo(String email) {
	  return userRepo.findByEmail(email) != null;
	}
	
	public LoggedInUserInfo processLoggedInUser() {
	  
    String emailId = "";
    boolean firstTimeLogin = false;
    boolean oauthLogin = false;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (principal instanceof User) {

      log.info("Login from system account");
      emailId = auth.getName();

    } else if (principal instanceof DefaultOidcUser) {

      log.info("Login from Google");
      DefaultOidcUser user = (DefaultOidcUser) principal;
      emailId = user.getEmail();
      oauthLogin = true;

    } else if (principal instanceof DefaultOAuth2User) {

      log.info("Login from GitHub");
      DefaultOAuth2User user = (DefaultOAuth2User) principal;
      Map<String, Object> myAttributes = user.getAttributes();
      emailId = (String) myAttributes.get("email");
      oauthLogin = true;

    }
    RegUser currentUser = userRepo.findByEmail(emailId);
    if (oauthLogin && currentUser == null) {
      //    Create a new user account in repo with default setting for roleMVC and roleREST
      RegUser usertoSave = new RegUser(0L, emailId, "", "ROLE_USER", true,
          "", "", "ROLE_BASIC");
      userRepo.save(usertoSave);
      firstTimeLogin = true;
    }
    return new LoggedInUserInfo(emailId, oauthLogin, firstTimeLogin, currentUser);
	}
	
	public boolean updateUserDetails(UpdateUserForm updateForm) {
	  
	  // First retrieve the id for the record of the current logged in user 
	  // from database table
	  LoggedInUserInfo  userInfo = processLoggedInUser();
    RegUser currentUser = userInfo.getCurrentUser();
    long userId = currentUser.getId();
    
    // If user is providing a new email to alter his existing email
    // check whether this new email is already in an existing account
    
    if (!(currentUser.getEmail().contentEquals(updateForm.getEmail()))) {
      
      if (checkEmailExistsInRepo(updateForm.getEmail())) {
        return false;
      }
      
    }
    
    // Update this record using the id and based on the info
    // from the update form
    userRepo.updateUserDetails(updateForm.getEmail(), updateForm.getFirstName(), updateForm.getLastName(), userId);
    return true;
	}
	
	public void deleteUserAccount(String email) {

    userRepo.deleteByEmail(email);

	}
	
	public void updateRoleForUserAccount(String email, String newRole) {
	  
    userRepo.updateRoleRest(email, newRole);
    
	}
	
	
	public List<RegUser> getAllUsers() {
	  
	  List<RegUser> mainUserList = userRepo.findAll();
	  return mainUserList;

	}
	
	
	public String generateNewJWT() {
	  
	  LoggedInUserInfo userInfo = processLoggedInUser();
	  RegUser currentUser = userInfo.getCurrentUser();
	  
	  log.info("expiry in days is " + expDays);
	  
	  String jwtString = jwtService.createJWT(currentUser.getEmail(), currentUser.getRoleREST(), convertMillis(expDays, expHours, expMinutes), jwtService.getSecretKey());
	  
	  return jwtString;
	  
	  
	}
	
	public ChangeDateForm initializeDateForm() {
	  
	  return new ChangeDateForm(expDays, expHours, expMinutes);
	}
	
	public void updateExpirationDate(ChangeDateForm dateForm) {
	  
	  expDays = dateForm.getExpDays();
	  expHours = dateForm.getExpHours();
	  expMinutes = dateForm.getExpMinutes();
	  
    try {
      // create and set properties into properties object
      Properties props = new Properties();
      props.setProperty("exp.days", expDays + "");
      props.setProperty("exp.hours", expHours + "");
      props.setProperty("exp.minutes", expMinutes + "");
      
      OutputStream out = new FileOutputStream( new File(JwtPropertiesFileName) );
      // write into it
      DefaultPropertiesPersister p = new DefaultPropertiesPersister();
      p.store(props, out, "Date of update");
    } catch (Exception e ) {
     e.printStackTrace();
    }
	  
	}
	
	
  private long convertMillis(int day, int hours, int minutes) {
    
    long result = 0L;
    result += minutes * 60000;
    result += hours * 60 * 60000;
    result += day * 24 * 60 * 60000;
    return result;
    
  }	

	
	

}
