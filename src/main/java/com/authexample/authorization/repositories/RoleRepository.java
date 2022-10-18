package com.authexample.authorization.repositories;

import com.authexample.authorization.models.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> { }
