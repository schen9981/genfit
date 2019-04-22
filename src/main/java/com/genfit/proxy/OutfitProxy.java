package com.genfit.proxy;

import com.genfit.clothing.Outfit;

public class OutfitProxy {

  // Instance variables describing the OutfitProxy instance
  private String id;
  private Outfit outfit;

  public OutfitProxy(String id) {
    if (id == null) {
      throw new NullPointerException();
    }
    this.id = id;
    this.outfit =null;
  }

}
