package com.authexample.authorization.repositories;

import com.authexample.authorization.models.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
  Optional<Account> findByLogin(String login);
}
