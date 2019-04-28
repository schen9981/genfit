package com.genfit.attribute.attributevals;

/**
 * Class to represent value of a color attribute.
 */
public class Color {
  private static final int HEX_BASE = 16;
  private static final int RGB_SHIFT = 8;
  private int colorVal;


  /**
   * Constructor for Color.
   *
   * @param colorVal RGB value of color as a hexadecimal number
   */
  public Color(int colorVal) {
    this.colorVal = colorVal;
  }

  @Override
  public String toString() {
    return Integer.toHexString(this.colorVal);
  }

  public static int convertToHexVal(int r, int g, int b) {
    r = Math.max(r, 0);
    g = Math.max(g, 0);
    b = Math.max(b, 0);

    if (r != 0) {
      r = Math.min(r, 255);
    }
    if (g != 0) {
      g = Math.min(g, 255);
    }
    if (b != 0) {
      b = Math.min(b, 255);
    }

    int rgbAsHex = r;
    rgbAsHex = rgbAsHex << RGB_SHIFT;
    rgbAsHex += g;
    rgbAsHex = rgbAsHex << RGB_SHIFT;
    rgbAsHex += b;
    return rgbAsHex;
  }
}
