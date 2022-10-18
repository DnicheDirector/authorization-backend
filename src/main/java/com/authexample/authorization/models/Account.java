package com.authexample.authorization.models;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {

  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID id;

  @Email(message = "invalid email")
  @NotBlank(message = "email shouldn't be empty")
  @Column(unique = true)
  private String login;
  private String password;

  @Length(min = 4, message = "invalid name length")
  private String name;

  private boolean enabled;
  private boolean expired;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private Status status;

  public void enableAccount() {
    this.role = Role.USER;
    this.status = Status.ACTIVE;
    this.enabled = true;
    this.expired = false;
  }

  public boolean isActive() {
    return status == Status.ACTIVE;
  }

}
