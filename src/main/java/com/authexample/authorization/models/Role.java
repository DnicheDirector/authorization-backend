package com.authexample.authorization.models;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role {
  ADMIN(Set.of(Permission.CREATE, Permission.EDIT, Permission.READ)),
  USER(Set.of(Permission.READ));

  private final Set<Permission> permissions;

  public Set<SimpleGrantedAuthority> getAuthorities(){
    return getPermissions().stream().
        map(permission->new SimpleGrantedAuthority(permission.name()))
        .collect(Collectors.toSet());
  }

}
