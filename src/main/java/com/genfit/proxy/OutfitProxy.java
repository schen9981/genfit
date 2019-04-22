package com.genfit.proxy;

import com.genfit.clothing.Outfit;
import com.genfit.database.Database;

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

  public Outfit geOutfit() {
    try {
      this.outfit = this.db.getOutfitBean(this.id);
    } catch (Exception e) {
      System.out.println("ERROR: Exception when getting OutfitBean");
    }
    return this.outfit;
  }

}
