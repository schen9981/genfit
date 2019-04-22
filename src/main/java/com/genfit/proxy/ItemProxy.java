package com.genfit.proxy;

import com.genfit.clothing.Item;

public class ItemProxy {

  // Instance variables describing the OutfitProxy instance
  private String id;
  private Item item;

  public ItemProxy(String id) {
    if (id == null) {
      throw new NullPointerException();
    }
    this.id = id;
    this.item =null;
  }
}
