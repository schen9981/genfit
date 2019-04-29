package com.genfit.attribute;

import com.genfit.attribute.attributevals.FormalityEnum;

import java.util.Objects;

public class FormalityAttribute implements Attribute<FormalityEnum> {
  private FormalityEnum formalityEnum;

  public FormalityAttribute(FormalityEnum formalityEnum) {
    this.formalityEnum = formalityEnum;
  }

  @Override
  public String getAttributeName() {
    return "formality";
  }

  @Override
  public FormalityEnum getAttributeVal() {
    return this.formalityEnum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FormalityAttribute that = (FormalityAttribute) o;
    return this.formalityEnum == that.formalityEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.formalityEnum);
  }

  @Override
  public String toString() {
    return "FormalityAttribute{" +
            this.formalityEnum.name() +
            '}';
  }
}
