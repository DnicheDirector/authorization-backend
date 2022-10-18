package com.authexample.authorization.models;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import com.authexample.authorization.models.field.Field;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

  @Column(unique = true)
  private String login;
  private String password;

  @Email(message = "invalid email")
  @NotBlank(message = "email shouldn't be empty")
  @Column(unique = true)
  private String email;

  @NotEmpty(message = "first name should not be empty")
  private String firstName;

  @NotEmpty(message = "last name should not be empty")
  private String lastName;

  private boolean enabled;
  private boolean expired;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<AccountGroup> groups;

  @Enumerated(EnumType.STRING)
  private Status status;

  public void enableAccount() {
//    this.roles = Set.of(Role.USER);
    this.status = Status.ACTIVE;
    this.enabled = true;
    this.expired = false;
  }

  public Set<SimpleGrantedAuthority> getAuthorities() {
    return getPermissions()
        .map(permission-> new SimpleGrantedAuthority(permission.getName()))
        .collect(toSet());
  }

  public Map<String, Set<String>> getAvailableFields() {
    return getPermissions()
        .flatMap(permission -> permission.getAvailableFields().stream())
        .collect(groupingBy(Field::getServiceName, mapping(Field::getName, toSet())));
  }

  private Stream<Permission> getPermissions() {
    Stream<Permission> rolesPermissions = getRoles().stream().flatMap(role -> role.getPermissions().stream());
    Stream<Permission> groupPermissions = getGroups().stream().flatMap(group -> group.getPermissions().stream());
    return Stream.concat(rolesPermissions, groupPermissions);
  }

  public boolean isActive() {
    return status == Status.ACTIVE;
  }

}
