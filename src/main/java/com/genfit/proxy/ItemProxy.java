package com.genfit.proxy;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.clothing.Item;
import com.genfit.database.Database;

public class ItemProxy {

  // Instance variables describing the OutfitProxy instance
  private String id;
  private Item item;
  private Database db;

  public ItemProxy(Database db, String id) {
    if (id == null || db == null) {
      throw new NullPointerException();
    }
    this.id = id;
    this.item = null;
    this.db = db;
  }

  public Item getItem() {
    try {
      this.item = this.db.getItemBean(this.id);
    } catch (Exception e) {
      System.out.println("ERROR: Exception when getting ItemBean");
    }
    return this.item;
  }

  private SeasonAttribute getWeatherAttribute() {
    return this.getItem().getWeatherAttribute();
  }

  private FormalityAttribute getFormalityAttribute() {
    return this.getItem().getFormalityAttribute();
  }

  private ColorAttribute getColorAttribute() {
    return this.getItem().getColorAttribute();
  }

  private TypeAttribute getTypeAttribute() {
    return this.getItem().getTypeAttribute();
  }

  public String getId() {
    return id;
  }
}
