package com.authexample.authorization.services.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JWTAuthenticationException extends AuthenticationException {

  private final HttpStatus httpStatus;

  public JWTAuthenticationException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }
}
