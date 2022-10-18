package com.authexample.authorization.models;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

  private final Account account;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return account.getAuthorities();
  }

  @Override
  public String getPassword() {
    return account.getPassword();
  }

  @Override
  public String getUsername() {
    return account.getLogin();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !account.isExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return account.isActive();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !account.isExpired();
  }

  @Override
  public boolean isEnabled() {
    return account.isEnabled();
  }
}
