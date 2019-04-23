package com.genfit.proxy;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.clothing.Item;
import com.genfit.database.Database;
import com.genfit.rankers.PatternAttrRanker;

import java.sql.SQLException;

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


  private Item getItem() {
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

  public String getId() {
    return this.id;
  }
}
