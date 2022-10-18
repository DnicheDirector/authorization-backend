package com.authexample.authorization.handler;

import com.authexample.authorization.exceptions.ConflictException;
import com.authexample.authorization.services.jwt.JWTAuthenticationException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request
  ) {
    final Map<String, String> errors = new HashMap<>();
    for (final FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException e){
    if(e.getRootCause() instanceof ConstraintViolationException){
      return handleConstraintViolationException((ConstraintViolationException) e.getRootCause());
    }
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e){
    final Map<String, String> errors = new HashMap<>();
    for (final ConstraintViolation<?> violation : e.getConstraintViolations()) {
      errors.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(JWTAuthenticationException.class)
  public ResponseEntity<?> handleJwtAuthenticationException(JWTAuthenticationException e){
    log.warn("JwtAuthenticationException: {}", e.getMessage());
    return new ResponseEntity<>(e.getMessage(),e.getHttpStatus());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<?> handleNoSuchElementException(UsernameNotFoundException e){
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<?> handleConflictException(ConflictException e){
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

}
