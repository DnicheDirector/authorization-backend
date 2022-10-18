package com.authexample.authorization.controllers;

import com.authexample.authorization.services.account.AccountService;
import com.authexample.authorization.views.RegistrationInputViewModel;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

  private final AccountService accountService;

  @PostMapping(value = "security/registration")
  @ResponseStatus(HttpStatus.CREATED)
  public void registration(@RequestBody @Valid RegistrationInputViewModel dto){
    accountService.createAccount(dto.getLogin(), dto.getPassword(), dto.getName());
  }

}
