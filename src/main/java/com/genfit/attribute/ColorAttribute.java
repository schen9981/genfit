package com.genfit.attribute;

import com.genfit.attribute.attributevals.Color;

public class ColorAttribute implements Attribute<Color>{
  private Color colorVal;
  public ColorAttribute(Color colorVal) {
    this.colorVal = colorVal;
  }

  @Override
  public Color getAttributeVal() {
    return colorVal;
  }
}
