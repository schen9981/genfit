package com.genfit.suggester;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.clothing.Outfit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for suggesting attributes given a combination of clothing.
 */
public class AttributeSuggester {
  /**
   * Private constructor for utility class.
   */
  private AttributeSuggester(){
  }

  /**
   * Returns a map of matching attributes given a partially or fully
   * specified outfit.
   *
   * @param o outfit to start matching on
   * @return a map from the attribute class to the possible values of the type
   */
  public Map<Class, List<Attribute>> getMatchingOutfitAttributes(Outfit o) {
    Map<Class, List<Attribute>> matches = new HashMap<>();
    List<Attribute> matchingSeasonAttrs = new LinkedList<>();
    List<Attribute> matchingFormalityAttrs = new LinkedList<>();
    List<Attribute> matchingPatternAttrs = new LinkedList<>();
    List<Attribute> matchingColorAttrs = new LinkedList<>();

    matches.put(ColorAttribute.class, matchingColorAttrs);
    return matches;
  }
}
