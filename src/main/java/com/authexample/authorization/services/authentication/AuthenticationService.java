package com.authexample.authorization.services.authentication;

import com.authexample.authorization.mappers.AccountMapper;
import com.authexample.authorization.models.Account;
import com.authexample.authorization.models.UserDetailsImpl;
import com.authexample.authorization.services.account.AccountService;
import com.authexample.authorization.services.jwt.JWTAuthenticationException;
import com.authexample.authorization.services.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JWTTokenProvider jwtTokenProvider;
  private final AccountService accountService;
  private final AccountMapper accountMapper;

  public AccountToken authenticate(String email, String password) {
    try {
      Account account = accountService.optionalAccountByLogin(email)
          .orElseThrow(()-> new UsernameNotFoundException("Account with such email doesn't exists"));

      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, password, account.getAuthorities())
      );

      return new AccountToken(
          accountMapper.toDTO(account),
          jwtTokenProvider.createToken(email, account.getRoles())
      );
    }
    catch (AuthenticationException e) {
      log.warn(e.getMessage(), e);
      throw new JWTAuthenticationException(
          e.getMessage().equals("User account is locked")
              ? "Account is locked"
              : "invalid email/password",
          HttpStatus.UNAUTHORIZED
      );
    }
  }

  public Account getPrincipal(){
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      return ((UserDetailsImpl) auth.getPrincipal()).getAccount();
    }
    catch (Exception e){
      throw new JWTAuthenticationException("Authentication Exception", HttpStatus.UNAUTHORIZED);
    }
  }
}
