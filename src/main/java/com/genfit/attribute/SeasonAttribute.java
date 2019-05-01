package com.genfit.attribute;

import com.genfit.attribute.attributevals.SeasonEnum;

import java.util.Objects;

public class SeasonAttribute implements Attribute<SeasonEnum> {
  private SeasonEnum seasonEnum;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    SeasonAttribute that = (SeasonAttribute) o;
    return this.seasonEnum == that.seasonEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.seasonEnum);
  }

  public SeasonAttribute(SeasonEnum seasonEnum) {
    this.seasonEnum = seasonEnum;
  }

  @Override
  public SeasonEnum getAttributeVal() {
    return this.seasonEnum;
  }

  @Override
  public String getAttributeName() {
    return "season";
  }

  @Override
  public String toString() {
    return "SeasonAttribute{" +
            this.seasonEnum.name() +
            '}';
  }
}
