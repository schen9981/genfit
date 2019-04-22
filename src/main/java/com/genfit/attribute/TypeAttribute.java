package com.genfit.attribute;

import com.genfit.attribute.attributevals.TypeEnum;

public class TypeAttribute implements Attribute<TypeEnum> {
  private TypeEnum typeEnum;

  public TypeAttribute(TypeEnum typeEnum) {
    this.typeEnum = typeEnum;
  }

  @Override
  public TypeEnum getAttributeVal() {
    return typeEnum;
  }
}
