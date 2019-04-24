package com.genfit.rankers;

import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.proxy.ItemProxy;

import java.util.LinkedList;
import java.util.List;

/**
 * Comparator for Pattern attribute.
 */
public class PatternAttrRanker implements AttributeRanker<PatternAttribute> {
  @Override
  public List<PatternAttribute> rankByAttribute(List<ItemProxy> items) {
    boolean patternFound = false;

    List<PatternAttribute> possiblePatterns = new LinkedList<>();

    // return empty list if no items
    if (items.size() > 0) {
      for (ItemProxy item : items) {
        PatternEnum itemPattern = item.getPatternAttribute().getAttributeVal();

        if (!itemPattern.equals(PatternEnum.SOLID)) {
          patternFound = true;
          break;
        }
      }

      if (patternFound) {
        possiblePatterns.add(new PatternAttribute(PatternEnum.SOLID));
      } else {
        possiblePatterns.add(new PatternAttribute(PatternEnum.STRIPED));
        possiblePatterns.add(new PatternAttribute(PatternEnum.CHECKERED));
        possiblePatterns.add(new PatternAttribute(PatternEnum.OTHER));
        possiblePatterns.add(new PatternAttribute(PatternEnum.SOLID));
      }
    }

    return possiblePatterns;
  }
}
