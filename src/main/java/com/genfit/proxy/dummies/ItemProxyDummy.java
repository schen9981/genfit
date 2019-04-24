package com.genfit.proxy.dummies;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.clothing.Item;
import com.genfit.proxy.ItemProxy;

public class ItemProxyDummy extends ItemProxy {
  private Item item;

  public ItemProxyDummy(Item item) {
    this.item = item;
  }

  @Override
  public SeasonAttribute getWeatherAttribute() {
    return this.item.getWeatherAttribute();
  }

  @Override
  public FormalityAttribute getFormalityAttribute() {
    return this.item.getFormalityAttribute();
  }

  @Override
  public ColorAttribute getColorAttribute() {
    return this.item.getColorAttribute();
  }

  @Override
  public TypeAttribute getTypeAttribute() {
    return this.item.getTypeAttribute();
  }

  @Override
  public PatternAttribute getPatternAttribute() {
    return this.item.getPatternAttribute();
  }

  @Override
  public int getId() {
    return this.item.getId();
  }

  @Override
  public Item getItem() {
    return this.item;
  }
}
