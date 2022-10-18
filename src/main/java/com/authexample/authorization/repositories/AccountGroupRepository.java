package com.authexample.authorization.repositories;

import com.authexample.authorization.models.AccountGroup;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountGroupRepository extends JpaRepository<AccountGroup, UUID> { }
