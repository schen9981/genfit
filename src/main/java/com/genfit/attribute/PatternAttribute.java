package com.genfit.attribute;

import com.genfit.attribute.attributevals.PatternEnum;

import java.util.Objects;

public class PatternAttribute implements Attribute<PatternEnum> {
  private PatternEnum patternEnum;

  public PatternAttribute(PatternEnum patternEnum) {
    this.patternEnum = patternEnum;
  }

  @Override
  public PatternEnum getAttributeVal() {
    return this.patternEnum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PatternAttribute that = (PatternAttribute) o;
    return this.patternEnum == that.patternEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.patternEnum);
  }

  @Override
  public String getAttributeName() {
    return "pattern";
  }

  @Override
  public String toString() {
    return "PatternAttribute{" +
            this.patternEnum.name() +
            '}';
  }
}
