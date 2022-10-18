package com.authexample.authorization.repositories;

import com.authexample.authorization.models.Permission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, UUID> { }
