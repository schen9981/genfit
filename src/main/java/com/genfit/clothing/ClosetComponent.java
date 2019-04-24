package com.genfit.clothing;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;

/**
 * Abstract class that wraps all the attribute/properties
 * of closet components (ie. item, outfit).
 */
public abstract class ClosetComponent {

  // properties of every attribute
  private SeasonAttribute season;
  private FormalityAttribute formality;
  private PatternAttribute pattern;
  private ColorAttribute color;
  private TypeAttribute type;

  /**
   * Constructor for a closet component.
   *
   * @param weather   - enum for weather property
   * @param formality - enum for formality property
   * @param pattern   - enum for pattern property
   * @param color     - enum for color property
   * @param type      - enum for type property
   */
  ClosetComponent(SeasonAttribute weather, FormalityAttribute formality,
                  PatternAttribute pattern, ColorAttribute color,
                  TypeAttribute type) {
    this.season = weather;
    this.formality = formality;
    this.pattern = pattern;
    this.color = color;
    this.type = type;
  }

  /**
   * Get the weather attribute for the closet component
   *
   * @return weather attribute
   */
  public SeasonAttribute getWeatherAttribute() {
    return this.season;
  }

  /**
   * Get the formality attribute for the closet component
   *
   * @return formality attribute
   */
  public FormalityAttribute getFormalityAttribute() {
    return this.formality;
  }

  /**
   * Get the pattern attribute for the closet component
   *
   * @return pattern attribute
   */
  public PatternAttribute getPatternAttribute() {
    return this.pattern;
  }

  /**
   * Get the color attribute for the closet component
   *
   * @return color attribute
   */
  public ColorAttribute getColorAttribute() {
    return this.color;
  }

  /**
   * Get the type attribute for the closet component
   *
   * @return type attribute
   */
  public TypeAttribute getTypeAttribute() {
    return this.type;
  }

}
