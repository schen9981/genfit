package com.genfit.proxy;

import com.genfit.database.Database;
import com.genfit.users.User;

import java.sql.SQLException;

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

  public User getUser() {
    try {
      this.user = this.db.getUserBean(this.email);
    } catch (Exception e) {
      System.out.println("ERROR: Exception when getting UserBean");
    }
    return this.user;
  }


}
