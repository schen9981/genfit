package com.genfit.attribute.attributevals;

public class LABColor {
  private double l;
  private double a;
  private double b;

  public LABColor(double l, double a, double b) {
    this.l = l;
    this.a = a;
    this.b = b;
  }

  /**
   * Getter method for l.
   *
   * @return value of l
   **/
  double getL() {
    return this.l;
  }

  /**
   * Getter method for a.
   *
   * @return value of a
   **/
  double getA() {
    return this.a;
  }

  /**
   * Getter method for b.
   *
   * @return value of b
   **/
  double getB() {
    return this.b;
  }
}
