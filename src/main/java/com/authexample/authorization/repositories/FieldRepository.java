package com.authexample.authorization.repositories;

import com.authexample.authorization.models.field.Field;
import com.authexample.authorization.models.field.FieldId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, FieldId> { }
