package com.genfit.proxy;

import com.genfit.clothing.Item;
import com.genfit.database.Database;

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


  // TODO: make this private
  public Item getItem() {
    try {
      this.item = this.db.getItemBean(this.id);
    } catch (Exception e) {
      System.out.println("ERROR: Exception when getting ItemBean");
    }
    return this.item;
  }
}
