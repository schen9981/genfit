package com.genfit.proxy;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.database.Database;
import com.google.common.collect.ImmutableMap;

import java.sql.SQLException;

public class OutfitProxy {

  // Instance variables describing the OutfitProxy instance
  private String id;
  private Outfit outfit;
  private Database db;

  public OutfitProxy(Database db, String id) {
    if (id == null || db == null) {
      throw new NullPointerException();
    }
    this.db = db;
    this.id = id;
    this.outfit = null;
  }

  private Outfit getOutfit() {
    try {
      this.outfit = this.db.getOutfitBean(this.id);
    } catch (Exception e) {
      System.out.println("ERROR: Exception when getting OutfitBean");
    }
    return this.outfit;
  }

  public ImmutableMap<TypeEnum, ItemProxy> getItems() {
    return this.getOutfit().getItems();
  }

}
