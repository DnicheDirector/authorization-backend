package com.authexample.authorization.models.field;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Field {

  @EmbeddedId
  private FieldId fieldId;

  public String getServiceName() {
    return this.fieldId.getServiceName();
  }

  public String getName() {
    return this.fieldId.getName();
  }
}
