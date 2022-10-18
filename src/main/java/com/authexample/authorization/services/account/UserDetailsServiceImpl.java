package com.authexample.authorization.services.account;

import com.authexample.authorization.models.UserDetailsImpl;
import com.authexample.authorization.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return accountRepository.findByLogin(username)
        .map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
