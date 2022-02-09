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
to get relevant user fields when updating details 
for an existing user.
We separate this from the actual RegUser class that will
be persisted to the backend database to avoid complications 
with the application of validation constraints. 
*/

public class UpdateUserForm {

	@Email
	private String email;

	@NotNull
	@Size(min = 2, max = 20, message = "First name should be between 2 to 20 characters")
	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only contain alphabets")
	private String firstName;

	@NotNull
	@Size(min = 2, max = 20, message = "Last name should be between 2 to 20 characters")
	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only contain alphabets")
	private String lastName;

}
