package com.genfit.rankers;

import com.genfit.attribute.SeasonAttribute;
import com.genfit.proxy.ItemProxy;

import java.util.List;

/**
 * Ranker for weather attribute.
 */
public class SeasonAttrRanker implements AttributeRanker<SeasonAttribute> {
  @Override
  public List<SeasonAttribute> rankByAttribute(List<ItemProxy> items) {
    for (ItemProxy item : items) {

    }
  }
}
