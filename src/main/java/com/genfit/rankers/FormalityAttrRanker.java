package com.genfit.rankers;

import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.proxy.ItemProxy;

import java.util.LinkedList;
import java.util.List;

/**
 * Ranker for formality attribute.
 */
public class FormalityAttrRanker implements AttributeRanker<FormalityAttribute> {
  @Override
  public List<FormalityAttribute> rankByAttribute(List<ItemProxy> items) {
    FormalityEnum curLowFormality = FormalityEnum.FORMAL;

    for (ItemProxy item : items) {
      if (curLowFormality.equals(FormalityEnum.ULTRA_CASUAL)) {
        break;
      } else {
        FormalityEnum itemFormality =
                item.getFormalityAttribute().getAttributeVal();

        // found a lower formality level
        if (itemFormality.compareTo(curLowFormality) < 0) {
          curLowFormality = itemFormality;
        }
      }
    }

    List<FormalityAttribute> toRet = new LinkedList<>();

    // return an empty list if there were no items in the list
    if (items.size() > 0) {
      toRet.add(new FormalityAttribute(curLowFormality));
      switch (curLowFormality) {
        case ULTRA_CASUAL:
          toRet.add(new FormalityAttribute(FormalityEnum.CASUAL));
          break;
        case CASUAL:
          break;
        case BUSINESS_CASUAL:
          toRet.add(new FormalityAttribute(FormalityEnum.BUSINESS_CASUAL));
          break;
        case FORMAL:
      }
    }
    return toRet;
  }
}
