package com.authexample.authorization.services.authentication;

import com.authexample.authorization.views.AccountOutputViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AccountToken {
  private final AccountOutputViewModel account;
  private final String token;
}
