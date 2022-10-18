package com.authexample.authorization.views;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOutputViewModel {
  private UUID id;
  private String login;
  private String email;
  private String firstName;
  private String lastName;
  private Set<RoleViewModel> roles;
  private Set<AccountGroupViewModel> groups;
}
