package com.authexample.authorization.views;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccountInputViewModel {
  @NotEmpty
  private String login;
  @NotEmpty
  private String password;
}
