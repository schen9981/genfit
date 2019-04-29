package com.genfit.attribute;

import com.genfit.attribute.attributevals.Color;

import java.util.Objects;

public class ColorAttribute implements Attribute<Color> {
  private Color colorVal;

  public ColorAttribute(Color colorVal) {
    this.colorVal = colorVal;
  }

  @Override
  public Color getAttributeVal() {
    return this.colorVal;
  }

  @Override
  public String getAttributeName() {
    return "color";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ColorAttribute that = (ColorAttribute) o;
    return Objects.equals(this.colorVal, that.colorVal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.colorVal);
  }

  @Override
  public String toString() {
    return "ColorAttribute{"
            + this.colorVal
            + '}';
  }
}
