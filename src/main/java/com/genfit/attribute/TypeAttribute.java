package com.genfit.attribute;

import com.genfit.attribute.attributevals.TypeEnum;

import java.util.Objects;

public class TypeAttribute implements Attribute<TypeEnum> {
  private TypeEnum typeEnum;

  public TypeAttribute(TypeEnum typeEnum) {
    this.typeEnum = typeEnum;
  }

  @Override
  public TypeEnum getAttributeVal() {
    return this.typeEnum;
  }

  @Override
  public String getAttributeName() {
    return "type";
  }

  @Override
  public String toString() {
    return "TypeAttribute{" +
            this.typeEnum.name() +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TypeAttribute that = (TypeAttribute) o;
    return this.typeEnum == that.typeEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.typeEnum);
  }
}
