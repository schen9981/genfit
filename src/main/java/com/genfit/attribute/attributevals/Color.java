package com.genfit.attribute.attributevals;

/**
 * Class to represent value of a color attribute.
 */
public class Color {
  private static final int HEX_BASE = 16;
  private static final int RGB_SHIFT = 8;
  private static final int MAX_RGB = 0xFFFFFF;
  private int colorVal;
  private int r;
  private int g;
  private int b;
  private double x;
  private double y;
  private double z;
  private LABColor labColor;

  /**
   * Constructor for Color.
   *
   * @param colorVal RGB value of color as a hexadecimal number
   */
  public Color(int colorVal) {
    // restricts to values of RGB range
    this.colorVal = Math.min(colorVal, MAX_RGB);
    this.colorVal = Math.max(colorVal, 0);
    this.labColor = null;
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

  /**
   * Getter method for labColor.
   *
   * @return value of labColor
   **/
  LABColor getLabColor() {
    return this.labColor;
  }

  @Override
  public String toString() {
    return Integer.toHexString(this.colorVal);
  }

  /**
   * Calculates perceptual difference in color using Delta E 76 spec from CIELAB
   * specification. Values range between 0 (same color) to 100 (opposite
   * colors). See references here:
   * http://zschuessler.github.io/DeltaE/learn/
   * https://www.easyrgb.com/en/math.php
   *
   * @param otherColor other color to calculate distance to
   * @return double representing distance
   */
  public double getLABDistance(Color otherColor) {
    if (this.labColor == null) {
      this.calculateLAB();
    }

    double thisL = this.labColor.getL();
    double thisA = this.labColor.getA();
    double thisB = this.labColor.getB();

    LABColor labColor = otherColor.getLabColor();
    double otherL = labColor.getL();
    double otherA = labColor.getA();
    double otherB = labColor.getB();

    //TODO: remove
    //more complicated DELTA E 94 standard, may implement later
    //    double deltaL = thisL - otherL;
    //    double C1 = Math.sqrt(Math.pow(thisA, 2.0) + Math.pow(thisB, 2.0));
    //    double C2 = Math.sqrt(Math.pow(thisA, 2.0) + Math.pow(otherB, 2.0));
    //    double deltaC = C1 - C2;
    //    double deltaA = thisA - otherA;
    //    double deltaB = thisB - otherB;
    //double SL =1;
    //double SC =1 +
    //
    //    double deltaHab = Math.sqrt(Math.pow(otherA, 2.0) + Math.pow(otherB, 2
    //    .0));
    //
    //    double deltaA = thisA - otherA;
    //    double deltaB = thisB - otherB;

    return Math.sqrt(Math.pow(thisL - otherL, 2.0)
            + Math.pow(thisA - otherA, 2.0)
            + Math.pow(thisB - otherB, 2.0));
  }

  private void calculateLAB() {
    if (this.labColor == null) {
      this.calculateXYZ();
    }

    //Reference-X, Y and Z refer to specific illuminants and observers.
//Common reference values are available below in this same page.
// Use reference values for Observer Illuminant class A-
//    Incandescent / tungsten

    //var_X = X / Reference - X
    //var_Y = Y / Reference - Y
    //var_Z = Z / Reference - Z
    //
    //if (var_X > 0.008856) var_X = var_X ^ (1 / 3)
    //else var_X = (7.787 * var_X) + (16 / 116)
    //if (var_Y > 0.008856) var_Y = var_Y ^ (1 / 3)
    //else var_Y = (7.787 * var_Y) + (16 / 116)
    //if (var_Z > 0.008856) var_Z = var_Z ^ (1 / 3)
    //else var_Z = (7.787 * var_Z) + (16 / 116)
    //
    //CIE - L * = (116 * var_Y) - 16
    //CIE - a * = 500 * (var_X - var_Y)
    //CIE - b * = 200 * (var_Y - var_Z)

  }

  private void calculateRGB() {
    if (this.r == 0) {
      String hexString = Integer.toHexString(this.colorVal);
      StringBuilder hexStringBuilder = new StringBuilder(hexString);
      // build to 6 characters
      while (hexStringBuilder.length() < 6) {
        hexStringBuilder.insert(0, "0");
      }

      String rStr = hexStringBuilder.substring(0, 2);
      String gStr = hexStringBuilder.substring(2, 4);
      String bStr = hexStringBuilder.substring(4, 6);

      this.r = Integer.parseInt(rStr, HEX_BASE);
      this.g = Integer.parseInt(gStr, HEX_BASE);
      this.b = Integer.parseInt(bStr, HEX_BASE);
    }
  }

  private void calculateXYZ() {
    if (this.r == 0) {
      this.calculateRGB();
    }

    double rDec = (this.r / 255);
    double gDec = (this.g / 255);
    double bDec = (this.b / 255);

    if (rDec > 0.04045) {
      rDec = Math.pow(((rDec + 0.055) / 1.055), 2.4);
    } else {
      rDec = rDec / 12.92;
    }
    if (gDec > 0.04045) {
      gDec = Math.pow(((gDec + 0.055) / 1.055), 2.4);
    } else {
      gDec = gDec / 12.92;
    }
    if (bDec > 0.04045) {
      bDec = Math.pow(((bDec + 0.055) / 1.055), 2.4);
    } else {
      bDec = bDec / 12.92;
    }

    rDec = rDec * 100;
    gDec = gDec * 100;
    bDec = bDec * 100;

    this.x = rDec * 0.4124 + gDec * 0.3576 + bDec * 0.1805;
    this.y = rDec * 0.2126 + gDec * 0.7152 + bDec * 0.0722;
    this.z = rDec * 0.0193 + gDec * 0.1192 + bDec * 0.9505;
  }
}

