package com.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.registration.forms.ChangeDateForm;
import com.registration.forms.MaintainUserForm;
import com.registration.forms.RegUserForm;
import com.registration.forms.UpdateUserForm;
import com.registration.model.LoggedInUserInfo;
import com.registration.model.RegUser;
import com.registration.service.JWTServiceProvider;
import com.registration.service.RegistrationService;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.matcher.ModifierMatcher.Mode;

@Controller
@Slf4j
public class RegistrationController {

  @Autowired
  RegistrationService regService;


  @GetMapping("/register")
  public String returnRegisterForm(Model model) {

    // Create a blank user form
    RegUserForm userForm = new RegUserForm("", "", "", "", "");
    model.addAttribute("userForm", userForm);

    return "register";
  }

  @PostMapping("/register")
  public String processRegisterForm(@Valid @ModelAttribute("userForm") RegUserForm userForm, BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {
      log.info("Binding result error : " + bindingResult);
      return "register";
    }
    log.info("Completed form details : " + userForm);

    if (regService.checkEmailExistsInRepo(userForm.getEmail())) {
      log.info("The email already exists");
      ObjectError error = new ObjectError("globalError", "That email is already registered");
      bindingResult.addError(error);
      return "register";
    }

    regService.createNewUserAccount(userForm);

    return "redirect:create-success";

  }

  @GetMapping("/mainmenu")
  public String showMainMenu(Model model) {
    log.info("successfully authenticated");
    
    LoggedInUserInfo userInfo = regService.processLoggedInUser();

    model.addAttribute("firstTimeLogin", userInfo.isFirstTimeLogin());
    model.addAttribute("email", userInfo.getEmail());

    return "mainmenu";
  }

  @GetMapping("/update")
  public String returnUpdateForm(Model model) {
    
    LoggedInUserInfo userInfo = regService.processLoggedInUser();
    RegUser currentUser = userInfo.getCurrentUser();
    UpdateUserForm updateForm = new UpdateUserForm(currentUser.getEmail(), currentUser.getFirstName(),currentUser.getLastName());

    model.addAttribute("updateForm", updateForm);

    return "update";
  }

  @PostMapping("/update")
  public String processUpdateForm(@Valid @ModelAttribute("updateForm") UpdateUserForm updateForm, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      log.info("Binding result error : " + bindingResult);
      return "update";
    }
    
    log.info("Received back update Form" + updateForm);
    boolean sucessfulUpdate = regService.updateUserDetails(updateForm);
    if (!sucessfulUpdate) {
      ObjectError error = new ObjectError("globalError", "That email is already registered");
      bindingResult.addError(error);
      return "update";
    }
    return "redirect:mainmenu";

  }

  @GetMapping("/maintain")
  public String returnMaintainForm(Model model) {
    
    List<RegUser> userList = regService.getAllUsers();

    for (RegUser user : userList) {
      log.info(user.toString());
    }
    model.addAttribute("users", userList);
    
    // Create a new form to be returned with default values
    MaintainUserForm maintainForm = new MaintainUserForm ("","update","ROLE_BASIC");
    model.addAttribute("maintainForm",maintainForm);

    return "maintain";
  }

  @PostMapping("/maintain")
  public String processMaintainForm(@Valid @ModelAttribute("maintainForm") MaintainUserForm maintainForm, Model model, BindingResult bindingResult) {
    
    if (!regService.checkEmailExistsInRepo(maintainForm.getEmail())) {
      ObjectError error = new ObjectError("globalError", "Please enter email for an existing user account");
      bindingResult.addError(error);

      List<RegUser> userList = regService.getAllUsers();
      model.addAttribute("users", userList);
      
      return "maintain";
    }
    
    log.info("maintainForm is " + maintainForm);
    
    if (maintainForm.getAction().contentEquals("delete")) {
      
      regService.deleteUserAccount(maintainForm.getEmail());
      
    } else if (maintainForm.getAction().contentEquals("update")) {
      
      regService.updateRoleForUserAccount(maintainForm.getEmail(), maintainForm.getNewRole());
      
    }
    
    return "redirect:maintain";

  }
  
  @GetMapping("/getjwt")
  public String returnJwt(Model model) {
    
    String jwtString = regService.generateNewJWT();
    
    model.addAttribute("jwtString", jwtString);
    
    return "getjwt";
  }
  
  @GetMapping("/changedate")
  public String returnChangeDateForm(Model model) {
    
    ChangeDateForm dateForm = regService.initializeDateForm();
    model.addAttribute("dateForm", dateForm);
    
    return "changedate";

  }
  
  @PostMapping("/changedate")
  public String processChangeDateForm(@Valid @ModelAttribute("dateForm") ChangeDateForm dateForm, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "changedate";
    }
    
    log.info("the date form is " + dateForm);
    
    if ((dateForm.getExpDays() + dateForm.getExpHours() + dateForm.getExpMinutes()) == 0)  {
      ObjectError error = new ObjectError("globalError", "Expiration duration cannot be 0");
      bindingResult.addError(error);
      return "changedate";
    }
    
    regService.updateExpirationDate(dateForm);
    return "redirect:mainmenu";
  }


}
