package com.authexample.authorization.mappers;

import com.authexample.authorization.models.Account;
import com.authexample.authorization.views.AccountOutputViewModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
  AccountOutputViewModel toDTO (Account account);
}
