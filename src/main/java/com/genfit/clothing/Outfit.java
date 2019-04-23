package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.proxy.ItemProxy;
import com.google.common.collect.ImmutableMap;

import java.util.Map;


public class Outfit extends ClosetComponent {

  // saves the list of items that the outfit has
  private String id;
  private Map<TypeEnum, ItemProxy> items;

  public Outfit(SeasonEnum weather, FormalityEnum formality,
                PatternEnum pattern,
                Color color, TypeEnum type, Map<TypeEnum, ItemProxy> items,
                String id) {
    // TODO: How to set attributes of outfit (some sort of average?)
    super(weather, formality, pattern, color, type);
    this.items = items;
    this.id = id;
  }

  /**
   * Gets a map (immutable) of the items that the outfit is composed of.
   *
   * @return map of items in the outfit
   */
  public Map<TypeEnum, ItemProxy> getItems() {
    ImmutableMap<TypeEnum, ItemProxy> immutable =
            ImmutableMap.copyOf(this.items);
    return immutable;
  }


}
