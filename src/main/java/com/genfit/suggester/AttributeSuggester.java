package com.genfit.suggester;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.rankers.AttributeRankerFactory;
import com.genfit.rankers.FormalityAttrRanker;

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
  private AttributeSuggester() {
  }

  /**
   * Returns a map of matching attributes given a partially or fully
   * specified outfit.
   *
   * @param o outfit to start matching on
   * @return a map from the Attribute class to the possible values of the type
   */
  public Map<Class, List<? extends Attribute>> getMatchingOutfitAttributes(Outfit o) {
    Map<Class, List<? extends Attribute>> matches = new HashMap<>();

    Map<TypeEnum, ItemProxy> outfitItems = o.getItems();
    AttributeRankerFactory arf = new AttributeRankerFactory();

    // Extract items of each clothing type
    for (TypeEnum clothingType : TypeEnum.values()) {
      ItemProxy item = outfitItems.get(clothingType);
      if (item != null) {
        arf.addItem(item);
      }
    }

    List<SeasonAttribute> matchingSeasonAttrs = new LinkedList<>();
    List<FormalityAttribute> matchingFormalityAttrs =
            arf.rankAttributes(new FormalityAttrRanker());
    List<PatternAttribute> matchingPatternAttrs = new LinkedList<>();
    List<ColorAttribute> matchingColorAttrs = new LinkedList<>();

    matches.put(SeasonAttribute.class, matchingSeasonAttrs);
    matches.put(FormalityAttribute.class, matchingFormalityAttrs);
    matches.put(PatternAttribute.class, matchingPatternAttrs);
    matches.put(ColorAttribute.class, matchingColorAttrs);

    return matches;
  }
}
