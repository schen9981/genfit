package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.proxy.ItemProxy;

public class ItemDistanceCalculator {
  public static final double similarityThresholdCutoff = 1.0;
  private static final double seasonWeight = 1.0;
  private static final double patternWeight = 1.0;
  private static final double formalityWeight = 1.0;
  private static final double colorWeight = 2.0 / 100.0;

  /**
   * Private constructor for utility class.
   */
  private ItemDistanceCalculator() {
  }

  /**
   * Calculates similarity "distance" to another item, lower scores mean more
   * similar.
   *
   * @return int indicating similarity score
   */
  public static double getSimilarity(ItemProxy thisItem, ItemProxy otherItem) {
    // different type will return largest possible distance
    if (!otherItem.getTypeAttribute().equals(thisItem.getTypeAttribute())) {
      return Integer.MAX_VALUE;
    } else {
      SeasonEnum otherSeason =
              otherItem.getWeatherAttribute().getAttributeVal();
      PatternEnum otherPattern =
              otherItem.getPatternAttribute().getAttributeVal();
      FormalityEnum otherFormality =
              otherItem.getFormalityAttribute().getAttributeVal();
      Color otherColor =
              otherItem.getColorAttribute().getAttributeVal();

      SeasonEnum thisSeason =
              thisItem.getWeatherAttribute().getAttributeVal();
      PatternEnum thisPattern =
              thisItem.getPatternAttribute().getAttributeVal();
      FormalityEnum thisFormality =
              thisItem.getFormalityAttribute().getAttributeVal();
      Color thisColor =
              thisItem.getColorAttribute().getAttributeVal();

      double seasonDist =
              Math.abs(thisSeason.ordinal() - otherSeason.ordinal());
      // handle wrap of seasons
      if (seasonDist >= (SeasonEnum.values().length - 1)) {
        seasonDist = 0.0;
      }

      double patternDist =
              Math.abs(thisPattern.ordinal() - otherPattern.ordinal());
      double formalityDist =
              Math.abs(thisFormality.ordinal() - otherFormality.ordinal());
      double colorDist = thisColor.getLABDistance(otherColor);

      double finalDist = (seasonWeight * seasonDist)
              + (patternWeight * patternDist)
              + (formalityWeight * formalityDist)
              + (colorWeight * colorDist);

      return finalDist;
    }
  }
}