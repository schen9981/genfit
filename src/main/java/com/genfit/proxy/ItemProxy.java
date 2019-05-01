package com.genfit.proxy;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.clothing.Item;
import com.genfit.database.Database;

import java.lang.reflect.Type;

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
      e.printStackTrace();
      System.out.println("ERROR: Exception when getting ItemBean");
    }
    return this.item;
  }

  public SeasonAttribute getWeatherAttribute() {
    Item i = this.getItem();
    if (i == null) {
      return null;
    } else {
      return i.getWeatherAttribute();
    }
  }

  public FormalityAttribute getFormalityAttribute() {
    Item i = this.getItem();
    if (i == null) {
      return null;
    } else {
      return i.getFormalityAttribute();
    }
  }

  public ColorAttribute getColorAttribute() {
    Item i = this.getItem();
    if (i == null) {
      return null;
    } else {
      return i.getColorAttribute();
    }
  }

  public TypeAttribute getTypeAttribute() {
    Item i = this.getItem();
    if (i == null) {
      return null;
    } else {
      return i.getTypeAttribute();
    }
  }

  public PatternAttribute getPatternAttribute() {
    Item i = this.getItem();
    if (i == null) {
      return null;
    } else {
      return i.getPatternAttribute();
    }
  }

  public Attribute getAttributeForItem(Class attributeClass) {
    if (attributeClass.equals(ColorAttribute.class)) {
      return this.getColorAttribute();
    } else if (attributeClass.equals(FormalityAttribute.class)) {
      return this.getFormalityAttribute();
    } else if (attributeClass.equals(PatternAttribute.class)) {
      return this.getPatternAttribute();
    } else if (attributeClass.equals(SeasonAttribute.class)) {
      return this.getWeatherAttribute();
    } else if (attributeClass.equals(TypeAttribute.class)) {
      return this.getTypeAttribute();
    } else {
      throw new IllegalArgumentException("ERROR: Invalid class "
              + attributeClass + "queried in getAttributeForitem");
    }
  }

  public int getId() {
    return this.id;
  }
}
