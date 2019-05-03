package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.SubtypeEnum;
import com.genfit.proxy.ItemProxy;

import java.util.Random;

public class ItemDistanceCalculator {
  public static final double SEASON_SIMILARITY_THRESHOLD = 1.0;
  public static final double PATTERN_SIMILARITY_THRESHOLD = 1.0;
  public static final double FORMALITY_SIMILARITY_THRESHOLD = 1.0;
  // 49 is the edge of perceptibility and the color is
  // weighted by 1/100
  public static final double COLOR_SIMILARITY_THRESHOLD = 0.6;
  public static final double SUBTYPE_SIMILARITY_THRESHOLD = 1.0;
  //public static final double TOTAL_SIMILARITY_THRESHOLD =
  //        SEASON_SIMILARITY_THRESHOLD + PATTERN_SIMILARITY_THRESHOLD
  //                + FORMALITY_SIMILARITY_THRESHOLD +
  //                COLOR_SIMILARITY_THRESHOLD;
  private static final double seasonWeight = 1.0;
  private static final double patternWeight = 1.0;
  private static final double formalityWeight = 1.0;
  private static final double subtypeWeight = 1.0;
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
      SubtypeEnum otherSubType =
              otherItem.getSubTypeAttribute().getAttributeVal();

      SeasonEnum thisSeason =
              thisItem.getWeatherAttribute().getAttributeVal();
      PatternEnum thisPattern =
              thisItem.getPatternAttribute().getAttributeVal();
      FormalityEnum thisFormality =
              thisItem.getFormalityAttribute().getAttributeVal();
      Color thisColor =
              thisItem.getColorAttribute().getAttributeVal();
      SubtypeEnum thisSubType =
              thisItem.getSubTypeAttribute().getAttributeVal();

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

      double subTypeDistance =
              Math.abs(thisSubType.ordinal() - otherSubType.ordinal());
      if ((thisSubType == SubtypeEnum.SANDALS &&
              otherSubType == SubtypeEnum.BOOTS) ||
              (thisSubType == SubtypeEnum.BOOTS &&
                      otherSubType == SubtypeEnum.SANDALS)) {
        subTypeDistance *= 2;
      } else if ((thisSubType == SubtypeEnum.SUIT &&
              otherSubType == SubtypeEnum.OUTER_COAT) ||
              (thisSubType == SubtypeEnum.OUTER_COAT &&
                      otherSubType == SubtypeEnum.SUIT)) {
        subTypeDistance *= 2;
      } else if ((thisSubType == SubtypeEnum.DRESS &&
              otherSubType == SubtypeEnum.PANTS) ||
              (thisSubType == SubtypeEnum.PANTS &&
                      otherSubType == SubtypeEnum.DRESS_SHOES)) {
        subTypeDistance *= 2;
      } else if ((thisSubType == SubtypeEnum.SHORTS &&
              otherSubType == SubtypeEnum.SKIRT) ||
              (thisSubType == SubtypeEnum.SKIRT &&
                      otherSubType == SubtypeEnum.SHORTS)) {
        subTypeDistance *= 2;
      }

      //TODO: remove
      //System.out.println("SEASON");
      //System.out.println(seasonWeight * seasonDist);
      //System.out.println("PATTERN");
      //System.out.println(patternWeight * patternDist);
      //System.out.println("FORMALITY");
      //System.out.println(formalityWeight * formalityDist);
      //System.out.println("COLOR");
      //System.out.println(colorWeight * colorDist);
      //System.out.println("SUBTYPE");
      //System.out.println(subtypeWeight * subTypeDistance);

      boolean criticalCategories =
              ((seasonWeight * seasonDist <= SEASON_SIMILARITY_THRESHOLD)
                      && (patternWeight * patternDist <= PATTERN_SIMILARITY_THRESHOLD)
                      && (formalityWeight * formalityDist <= FORMALITY_SIMILARITY_THRESHOLD)
                      && (subtypeWeight * subTypeDistance) <= SUBTYPE_SIMILARITY_THRESHOLD);

      boolean colorVerySimilar =
              (colorWeight * colorDist <= COLOR_SIMILARITY_THRESHOLD);
      boolean colorSlightlySimilar =
              (colorWeight * colorDist <= (2 * COLOR_SIMILARITY_THRESHOLD));

      // definitely need the critical categories of similarity
      if (criticalCategories) {
        // if color also similar then yes
        if (colorVerySimilar) {
          return true;
          // randomness factor when color on the border line
        } else if (colorSlightlySimilar) {
          Random r = new Random();
          return (r.nextInt(100) <= 50);
        }
      }
    }
    return false;
  }
}
