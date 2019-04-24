package com.genfit.attribute;

import com.genfit.attribute.attributevals.SeasonEnum;

public class SeasonAttribute implements Attribute<SeasonEnum> {
  private SeasonEnum seasonEnum;

  public SeasonAttribute(SeasonEnum seasonEnum) {
    this.seasonEnum = seasonEnum;
  }

  @Override
  public SeasonEnum getAttributeVal() {
    return this.seasonEnum;
  }

  @Override
  public String toString() {
    return "SeasonAttribute{" +
            this.seasonEnum.name() +
            '}';
  }
}
