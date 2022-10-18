package com.authexample.authorization.controllers;

import com.authexample.authorization.mappers.AccountMapper;
import com.authexample.authorization.models.Account;
import com.authexample.authorization.services.authentication.AccountToken;
import com.authexample.authorization.services.authentication.AuthenticationService;
import com.authexample.authorization.views.AccountInputViewModel;
import com.authexample.authorization.views.AccountOutputViewModel;
import com.authexample.authorization.views.MultiServiceAccountViewModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final AccountMapper accountMapper;

  @PostMapping(value = "security/login")
  @ResponseStatus(HttpStatus.OK)
  public AccountToken authenticate(@RequestBody @Valid AccountInputViewModel dto) {
    return authenticationService.authenticate(dto.getLogin(), dto.getPassword());
  }

  @GetMapping("security/authentication-principal")
  @PreAuthorize("hasAuthority('READ_INVOICE_PERMISSION')")
  @ResponseStatus(HttpStatus.OK)
  public AccountOutputViewModel authenticationPrincipal(){
    Account account = authenticationService.getPrincipal();
    return accountMapper.toDTO(account);
  }

  @GetMapping("security/multi-service-principal")
  @PreAuthorize("hasAuthority('READ_INVOICE_PERMISSION')")
  @ResponseStatus(HttpStatus.OK)
  public MultiServiceAccountViewModel multiServicePrincipal(){
    Account account = authenticationService.getPrincipal();
    return new MultiServiceAccountViewModel(
        accountMapper.toDTO(account),
        account.getAvailableFields()
    );
  }

  @PostMapping("security/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(HttpServletRequest request, HttpServletResponse response){
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request,response,null);
  }
}
