package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.proxy.ItemProxy;

public class ItemDistanceCalculator {
  public static final double SEASON_SIMILARITY_THRESHOLD = 1.1;
  public static final double PATTERN_SIMILARITY_THRESHOLD = 1.2;
  public static final double FORMALITY_SIMILARITY_THRESHOLD = 1.2;
  // 0.5 for color because 49 is the edge of perceptibility and the color is
  // weighted by 1/100
  public static final double COLOR_SIMILARITY_THRESHOLD = 0.5;
  public static final double TOTAL_SIMILARITY_THRESHOLD =
          SEASON_SIMILARITY_THRESHOLD + PATTERN_SIMILARITY_THRESHOLD
                  + FORMALITY_SIMILARITY_THRESHOLD + COLOR_SIMILARITY_THRESHOLD;
  private static final double seasonWeight = 1.0;
  private static final double patternWeight = 1.0;
  private static final double formalityWeight = 1.0;
  private static final double colorWeight = 1.0 / 100.0;

  /**
   * Private constructor for utility class.
   */
  private ItemDistanceCalculator() {
  }

  /**
   * Calculates whether two items are similar.
   *
   * @param thisItem
   * @param otherItem
   * @return boolean indicating whether about is true
   */
  public static boolean areSimilar(ItemProxy thisItem, ItemProxy otherItem) {
    // different type will return largest possible distance
    if (!otherItem.getTypeAttribute().equals(thisItem.getTypeAttribute())) {
      return false;
    } else {
      SeasonEnum otherSeason =
              otherItem.getWeatherAttribute().getAttributeVal();
      PatternEnum otherPattern =
              otherItem.getPatternAttribute().getAttributeVal();
      FormalityEnum otherFormality =
              otherItem.getFormalityAttribute().getAttributeVal();
      Color otherColor =
              otherItem.getColorAttribute().getAttributeVal();
      // TODO: add subtype distance calculation

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
      // larger difference between casual and business casual
      if (thisFormality.ordinal() < FormalityEnum.BUSINESS_CASUAL.ordinal() &&
              otherFormality.ordinal() >= FormalityEnum.BUSINESS_CASUAL.ordinal()) {
        formalityDist *= 2;
      } else if (otherFormality.ordinal() < FormalityEnum.BUSINESS_CASUAL.ordinal() &&
              thisFormality.ordinal() >= FormalityEnum.BUSINESS_CASUAL.ordinal()) {
        formalityDist *= 2;
      }

      double colorDist = thisColor.getLABDistance(otherColor);

      return ((seasonWeight * seasonDist <= SEASON_SIMILARITY_THRESHOLD)
              && (patternWeight * patternDist <= PATTERN_SIMILARITY_THRESHOLD)
              && (formalityWeight * formalityDist <= FORMALITY_SIMILARITY_THRESHOLD)
              && (colorWeight * colorDist <= COLOR_SIMILARITY_THRESHOLD));
    }
  }
}
