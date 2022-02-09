package com.registration.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

/*
We create this class to provide the form backing object
to get relevant info when maintaining existing user accounts
*/

public class MaintainUserForm {
  
  @Email
  private String email;
  
  private String action;
  private String newRole;

}
