package com.genfit.clothing;

import java.util.Map;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.proxy.ItemProxy;
import com.google.common.collect.ImmutableMap;

public class Outfit extends ClosetComponent {

  // saves the list of items that the outfit has
  private String id;
  private Map<TypeEnum, ItemProxy> items;
  private String name;
  private SeasonAttribute season;
  private FormalityAttribute formality;
  private PatternAttribute pattern;
  private ColorAttribute color;

  public Outfit(String id, String name, Map<TypeEnum, ItemProxy> items) {
    // TODO: How to set attributes of outfit (some sort of average?)
//		super(weather, formality, pattern, color, null);
    super(null, null, null, null, null);
    this.items = items;
    this.id = id;
    this.name = name;
  }

  /**
   * Gets a map (immutable) of the items that the outfit is composed of.
   *
   * @return map of items in the outfit
   */
  public ImmutableMap<TypeEnum, ItemProxy> getItems() {
    ImmutableMap<TypeEnum, ItemProxy> immutable = ImmutableMap
        .copyOf(this.items);
    return immutable;
  }

}
