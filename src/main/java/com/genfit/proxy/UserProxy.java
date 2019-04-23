package com.genfit.proxy;

import com.genfit.database.Database;
import com.genfit.users.User;

import java.util.List;

public class UserProxy {

  private String email;
  private User user;
  private Database db;

  public UserProxy(Database db, String email) {
    if (email == null || db == null) {
      throw new NullPointerException();
    }
    this.db = db;
    this.email = email;
    this.user = null;
  }

  private User getUser() {
    if (this.user == null) {
      try {
        this.user = this.db.getUserBean(this.email);
      } catch (Exception e) {
        System.out.println("ERROR: Exception when getting UserBean");
      }
    }
    return this.user;
  }


  public String getEmail() {
    return this.email;
  }

  public String getId() {
    return this.getUser().getId();
  }

  public String getName() {
    return this.getUser().getName();
  }

  public List<ItemProxy> getItems() {
    return this.getUser().getItems();
  }

  public List<OutfitProxy> getOutfits() {
    return this.getUser().getOutfits();
  }

}
