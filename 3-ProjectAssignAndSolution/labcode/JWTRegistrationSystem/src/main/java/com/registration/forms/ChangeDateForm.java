package com.registration.forms;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ChangeDateForm {
  
  @Min(value = 0, message = "Days must be 0 or more")
  private int expDays;
  
  @Min(value = 0, message = "Hours must be 0 or more")
  private int expHours;
  
  @Min(value = 0, message = "Minutes must be 0 or more")
  private int expMinutes;

}
