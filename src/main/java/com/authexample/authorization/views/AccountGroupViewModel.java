package com.authexample.authorization.views;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class AccountGroupViewModel {
  private UUID id;
  private String name;
  private Set<PermissionViewModel> permissions;
}
