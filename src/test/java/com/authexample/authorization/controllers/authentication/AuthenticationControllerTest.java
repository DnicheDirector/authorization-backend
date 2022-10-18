package com.authexample.authorization.controllers.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.authexample.authorization.controllers.BaseTestWrapper;
import com.authexample.authorization.models.Account;
import com.authexample.authorization.repositories.AccountRepository;
import com.authexample.authorization.services.authentication.AccountToken;
import com.authexample.authorization.views.AccountInputViewModel;
import com.authexample.authorization.views.AccountOutputViewModel;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationControllerTest extends BaseTestWrapper {

  private static Account account;

  private static final String password = "12345678";

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

  @BeforeAll
  public static void initAccount() {
    account = Account.builder()
        .login("example@gmail.com")
        .password(passwordEncoder.encode(password))
        .firstName("example")
        .build();
    account.enableAccount();
  }

  @MockBean
  private AccountRepository accountRepository;

  @BeforeEach
  public void mockAccount() {
    when(accountRepository.findByLogin(account.getLogin()))
        .thenReturn(Optional.of(account));
  }

  @Test
  public void successAuthenticateIfAccountExists() {
    AccountInputViewModel viewModel = new AccountInputViewModel();
    viewModel.setLogin(account.getLogin());
    viewModel.setPassword(password);

    ResponseEntity<AccountToken> response =
        exchange("security/login", viewModel, HttpMethod.POST, AccountToken.class);

    AccountToken accountToken = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(accountToken);
    assertNotNull(accountToken.getToken());
    assertNotNull(accountToken.getAccount());
    assertEquals(account.getLogin(), accountToken.getAccount().getLogin());
  }

  @Test
  public void failedAuthenticateIfAccountWithSuchCredentialsNotExists() {
    AccountInputViewModel viewModel = new AccountInputViewModel();
    viewModel.setLogin("failed@gmail.com");
    viewModel.setPassword("123488448");

    ResponseEntity<Void> response =
        exchange("security/login", viewModel, HttpMethod.POST, Void.class);


    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void successGetPrincipalIfTokenIsValid() {
    ResponseEntity<AccountOutputViewModel> response =
        exchangeWithAuthentication("security/authentication-principal", null, account, HttpMethod.GET, AccountOutputViewModel.class);

    AccountOutputViewModel responseBody = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
    assertEquals(account.getLogin(), responseBody.getLogin());
  }

  @Test
  public void failedGetPrincipalIfTokenIsEmpty() {
    ResponseEntity<Void> response =
        exchange("security/authentication-principal", null, HttpMethod.GET, Void.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void successLogout() {
    ResponseEntity<Void> response =
        exchange("security/logout", null, HttpMethod.POST, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }


}
