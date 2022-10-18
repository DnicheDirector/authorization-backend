package com.authexample.authorization.services.account;

import com.authexample.authorization.exceptions.ConflictException;
import com.authexample.authorization.models.Account;
import com.authexample.authorization.repositories.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  public Optional<Account> optionalAccountByLogin(String login) {
    return accountRepository.findByLogin(login);
  }

  private Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Transactional
  public void createAccount(String login, String password, String name) {
    Optional<Account> account = optionalAccountByLogin(login);
    if (account.isEmpty()) {
      Account newAccount = Account.builder()
          .login(login)
          .password(passwordEncoder.encode(password))
          .firstName(name)
          .build();

      newAccount.enableAccount();

      saveAccount(newAccount);
    } else {
      throw new ConflictException("Account with specified login is already present");
    }
  }
}
