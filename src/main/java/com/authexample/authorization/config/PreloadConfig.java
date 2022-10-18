package com.authexample.authorization.config;

import com.authexample.authorization.models.Account;
import com.authexample.authorization.models.AccountGroup;
import com.authexample.authorization.models.Permission;
import com.authexample.authorization.models.Role;
import com.authexample.authorization.models.field.Field;
import com.authexample.authorization.models.field.FieldId;
import com.authexample.authorization.repositories.AccountGroupRepository;
import com.authexample.authorization.repositories.AccountRepository;
import com.authexample.authorization.repositories.FieldRepository;
import com.authexample.authorization.repositories.PermissionRepository;
import com.authexample.authorization.repositories.RoleRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PreloadConfig {

  private static final String INVOICE_SERVICE_NAME = "INVOICE_SERVICE";
  private static final String BILLING_SERVICE_NAME = "BILLING_SERVICE";
  private static final String TRUCKS_SERVICE_NAME = "TRUCKS_SERVICE";

  private static final String ALIAS_FIELD = "alias";
  private static final String ISSUED_AT = "issuedAt";
  private static final String PLACED_AT = "placedAt";
  private static final String PAYMENT_PERCENTAGE = "paymentPercentage";
  private static final String DISCOUNT = "discount";

  private static final String BILLING_ACCOUNT = "billingAccount";

  private static final String TRUCK_NAME = "truckName";
  private static final String WHEELS = "wheels";

  @Bean
  public CommandLineRunner commandLineRunner(
      AccountGroupRepository accountGroupRepository,
      AccountRepository accountRepository,
      FieldRepository fieldRepository,
      PermissionRepository permissionRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder
  ) {
    return args -> {
      Field invoiceField1 = new Field(new FieldId(INVOICE_SERVICE_NAME, ALIAS_FIELD));
      Field invoiceField2 = new Field(new FieldId(INVOICE_SERVICE_NAME, ISSUED_AT));
      Field invoiceField3 = new Field(new FieldId(INVOICE_SERVICE_NAME, PLACED_AT));
      Field invoiceField4 = new Field(new FieldId(INVOICE_SERVICE_NAME, PAYMENT_PERCENTAGE));
      Field invoiceField5 = new Field(new FieldId(INVOICE_SERVICE_NAME, DISCOUNT));

      Field billingField = new Field(new FieldId(BILLING_SERVICE_NAME, BILLING_ACCOUNT));

      fieldRepository.saveAll(Set.of(invoiceField1, invoiceField2, invoiceField3, invoiceField4, invoiceField5, billingField));

      Permission writeInvoicePermission = Permission.builder()
          .name("WRITE_INVOICE_PERMISSION")
          .availableFields(Set.of(invoiceField1, invoiceField2, invoiceField3, invoiceField4, invoiceField5))
          .build();

      Permission readInvoicePermission = Permission.builder()
          .name("READ_INVOICE_PERMISSION")
          .availableFields(Set.of(invoiceField1, invoiceField2))
          .build();

      Permission editInvoicePermission = Permission.builder()
          .name("EDIT_INVOICE_PERMISSION")
          .availableFields(Set.of(invoiceField1, invoiceField2, invoiceField3))
          .build();

      Permission readBillingPermissions = Permission.builder()
          .name("READ_BILLING_PERMISSION")
          .availableFields(Set.of(billingField))
          .build();


      permissionRepository.saveAll(Set.of(writeInvoicePermission, readInvoicePermission, editInvoicePermission, readBillingPermissions));

      Role adminRole = Role.builder()
          .name("ADMIN")
          .permissions(Set.of(writeInvoicePermission, readInvoicePermission, editInvoicePermission))
          .build();

      Role userRole = Role.builder()
          .name("USER")
          .permissions(Set.of(readInvoicePermission))
          .build();

      roleRepository.saveAll(Set.of(adminRole, userRole));

      AccountGroup accountGroup = AccountGroup.builder()
          .name("EDIT_GROUP")
          .permissions(Set.of(editInvoicePermission, readBillingPermissions))
          .build();

      accountGroupRepository.save(accountGroup);

      Account account = Account.builder()
          .email("example@gmail.com")
          .firstName("incognito")
          .lastName("123")
          .password(passwordEncoder.encode("12345678"))
          .login("login")
          .roles(Set.of(adminRole))
          .groups(Set.of(accountGroup))
          .build();
      account.enableAccount();

      accountRepository.save(account);
    };
  }
}
