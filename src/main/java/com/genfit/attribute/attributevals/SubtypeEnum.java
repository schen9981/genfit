package com.genfit.attribute.attributevals;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericwang on 2019/5/2.
 */
public enum SubtypeEnum {
  // Please don't change the order of the items here, it's used for item
  // distance calculation
  OUTER_COAT, SUIT,
  SHIRT_BLOUSE, T_SHIRT, SWEATER, JACKET,
  PANTS, DRESS, SKIRT, SHORTS,
  DRESS_SHOES, SNEAKERS, BOOTS, SANDALS;

  public static final ImmutableMap<SubtypeEnum, TypeEnum> TYPE_CATEGORIES;

  static {
    Map<SubtypeEnum, TypeEnum> tempMap = new HashMap<>();
    tempMap.put(OUTER_COAT, TypeEnum.OUTER);
    tempMap.put(SUIT, TypeEnum.OUTER);
    tempMap.put(SHIRT_BLOUSE, TypeEnum.TOP);
    tempMap.put(T_SHIRT, TypeEnum.TOP);
    tempMap.put(SWEATER, TypeEnum.TOP);
    tempMap.put(JACKET, TypeEnum.TOP);
    tempMap.put(PANTS, TypeEnum.BOTTOM);
    tempMap.put(SKIRT, TypeEnum.BOTTOM);
    tempMap.put(DRESS, TypeEnum.BOTTOM);
    tempMap.put(SHORTS, TypeEnum.BOTTOM);
    tempMap.put(SNEAKERS, TypeEnum.SHOES);
    tempMap.put(BOOTS, TypeEnum.SHOES);
    tempMap.put(SANDALS, TypeEnum.SHOES);
    tempMap.put(DRESS_SHOES, TypeEnum.SHOES);
    TYPE_CATEGORIES = ImmutableMap.copyOf(tempMap);
  }
}
