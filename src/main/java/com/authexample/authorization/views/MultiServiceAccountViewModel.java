package com.authexample.authorization.views;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultiServiceAccountViewModel {
  private AccountOutputViewModel accountOutputViewModel;
  private Map<String, Set<String>> availableFields;
}
