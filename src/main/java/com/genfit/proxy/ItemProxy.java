package com.genfit.proxy;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.clothing.Item;
import com.genfit.database.Database;

public class ItemProxy {

  // Instance variables describing the OutfitProxy instance
  private int id;
  private Item item;
  private Database db;

  /**
   * Default constructor, should probably not be used.
   */
  public ItemProxy() {
    this.db = null;
    this.item = null;
  }

  public ItemProxy(Database db, int id) {
    if (db == null) {
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

  public SeasonAttribute getWeatherAttribute() {
    return this.getItem().getWeatherAttribute();
  }

  public FormalityAttribute getFormalityAttribute() {
    return this.getItem().getFormalityAttribute();
  }

  public ColorAttribute getColorAttribute() {
    return this.getItem().getColorAttribute();
  }

  public TypeAttribute getTypeAttribute() {
    return this.getItem().getTypeAttribute();
  }

  public PatternAttribute getPatternAttribute() {
    return this.getItem().getPatternAttribute();
  }

  public int getId() {
    return this.id;
  }
}
