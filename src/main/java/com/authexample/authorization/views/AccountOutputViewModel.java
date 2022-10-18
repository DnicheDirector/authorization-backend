package com.authexample.authorization.views;

import com.authexample.authorization.models.Role;
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
  private String name;
  private Role role;
}
