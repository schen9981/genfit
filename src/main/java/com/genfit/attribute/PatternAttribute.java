package com.genfit.attribute;

import com.genfit.attribute.attributevals.PatternEnum;

public class PatternAttribute implements Attribute<PatternEnum> {
  private PatternEnum patternEnum;

  public PatternAttribute(PatternEnum patternEnum) {
    this.patternEnum = patternEnum;
  }

  @Override
  public PatternEnum getAttributeVal() {
    return patternEnum;
  }
}
