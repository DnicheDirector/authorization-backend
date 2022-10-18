package com.authexample.authorization.views;

import java.util.UUID;
import lombok.Data;

@Data
public class PermissionViewModel {
  private UUID id;
  private String name;
}
