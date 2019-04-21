package com.genfit.attribute;

import com.genfit.attribute.attributevals.FormalityEnum;

public class FormalityAttribute implements Attribute<FormalityEnum> {
  private FormalityEnum formalityEnum;

  public FormalityAttribute(FormalityEnum formalityEnum) {
    this.formalityEnum = formalityEnum;
  }

  @Override
  public FormalityEnum getAttributeVal() {
    return formalityEnum;
  }
}
