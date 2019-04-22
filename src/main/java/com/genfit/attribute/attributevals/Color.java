package com.genfit.attribute.attributevals;

public class Color {
  private int colorVal;

  public Color(int colorVal) {
    this.colorVal = colorVal;
  }

  @Override
  public String toString() {
    return Integer.toHexString(colorVal);
  }
}
