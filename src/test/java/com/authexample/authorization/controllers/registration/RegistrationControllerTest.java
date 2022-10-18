package com.authexample.authorization.controllers.registration;

import com.authexample.authorization.controllers.BaseTestWrapper;
import com.authexample.authorization.views.AccountInputViewModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationControllerTest extends BaseTestWrapper {

  @Test
  public void successRegistrationIfValidData() {
    AccountInputViewModel viewModel = new AccountInputViewModel();
    viewModel.setLogin("test@gmail.com");
    viewModel.setPassword("12345678");

    ResponseEntity<Void> response = exchange(
        "security/registration", viewModel, HttpMethod.POST, Void.class
    );

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  public void failedRegistrationIfEmailAndPasswordAreInvalid() {
    AccountInputViewModel viewModel = new AccountInputViewModel();
    viewModel.setLogin("test.com");
    viewModel.setPassword("1234");

    ResponseEntity<Void> response = exchange(
        "security/registration", viewModel, HttpMethod.POST, Void.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void failedRegistrationIfAccountAlreadyExists() {
    AccountInputViewModel viewModel = new AccountInputViewModel();
    viewModel.setLogin("first@gmail.com");
    viewModel.setPassword("12345678");

    ResponseEntity<Void> firstResponse = exchange(
        "security/registration", viewModel, HttpMethod.POST, Void.class
    );

    ResponseEntity<Void> secondResponse = exchange(
        "security/registration", viewModel, HttpMethod.POST, Void.class
    );

    assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
    assertEquals(HttpStatus.BAD_REQUEST, secondResponse.getStatusCode());
  }
}
