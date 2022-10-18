package com.authexample.authorization.views;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegistrationInputViewModel {
  @NotEmpty(message = "email shouldn't be empty")
  private String login;
  @Length(min = 8, message = "Password should be no less than 8 characters")
  private String password;
  @Length(min = 4, message = "invalid name length")
  private String name;
}
