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

  /**
   * Constructor for Color using RGB.
   *
   * @param r red value
   * @param g green value
   * @param b blue value
   */
  public Color(int r, int g, int b) {
    // restricts to values of RGB range
    this.colorVal = convertToHexVal(r, g, b);
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
    if (this.labColor == null) {
      this.calculateLAB();
    }
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

    return Math.sqrt(Math.pow(thisL - otherL, 2.0)
            + Math.pow(thisA - otherA, 2.0)
            + Math.pow(thisB - otherB, 2.0));
  }

  private void calculateLAB() {
    if (this.x == 0) {
      this.calculateXYZ();
    }

    //Reference-X, Y and Z refer to specific illuminants and observers.
    //Use reference values for Observer Illuminant class A-Incandescent /
    // tungsten

    double tempX = this.x / 95.047;
    double tempY = this.y / 100.0;
    double tempZ = this.z / 108.883;

    if (tempX > 0.008856) {
      tempX = Math.pow(tempX, (1.0 / 3));
    } else {
      tempX = (7.787 * tempX) + (16.0 / 116);
    }
    if (tempY > 0.008856) {
      tempY = Math.pow(tempY, (1.0 / 3));
    } else {
      tempY = (7.787 * tempY) + (16.0 / 116);
    }
    if (tempZ > 0.008856) {
      tempZ = Math.pow(tempZ, (1.0 / 3));
    } else {
      tempZ = (7.787 * tempZ) + (16.0 / 116);
    }

    double l = (116 * tempY) - 16;
    double a = 500 * (tempX - tempY);
    double b = 200 * (tempY - tempZ);

    this.labColor = new LABColor(l, a, b);

  }

  private void calculateRGB() {
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

  private void calculateXYZ() {
    if (this.r == 0) {
      this.calculateRGB();
    }

    double rDec = (this.r / 255.0);
    double gDec = (this.g / 255.0);
    double bDec = (this.b / 255.0);

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

    this.x = (rDec * 0.4124) + (gDec * 0.3576) + (bDec * 0.1805);
    this.y = (rDec * 0.2126) + (gDec * 0.7152) + (bDec * 0.0722);
    this.z = (rDec * 0.0193) + (gDec * 0.1192) + (bDec * 0.9505);
  }
}

