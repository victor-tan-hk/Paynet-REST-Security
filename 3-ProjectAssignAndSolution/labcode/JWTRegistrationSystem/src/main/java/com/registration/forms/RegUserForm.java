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
to get relevant user fields when registering a new user.
We separate this from the actual RegUser class that will
be persisted to the backend database to avoid complications 
with the application of validation constraints. This is particularly
true for passwords, which will be stored in encoded sequence in the 
password field when used for storage
*/

@PasswordMatches
public class RegUserForm {

	@Email
	private String email;

	@NotNull
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,12}$", message = "Password must contain at least 1 digit, 1 uppercase alphabet, 1 lower case alphabet and be between 4 to 12 characters")
	private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;
	
	@NotNull
	@Size(min = 2, max = 20, message = "First name should be between 2 to 20 characters")
	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only contain alphabets")
	private String firstName;

	@NotNull
	@Size(min = 2, max = 20, message = "Last name should be between 2 to 20 characters")
	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only contain alphabets")
	private String lastName;

}
