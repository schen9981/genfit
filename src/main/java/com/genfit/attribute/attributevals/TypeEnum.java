package com.genfit.attribute.attributevals;

public enum TypeEnum {
  OUTER("outer"), TOP("top"), BOTTOM("bottom"), SHOES("feet");

  public String label;

  private TypeEnum(String label) {
    this.label = label;
  }
}
