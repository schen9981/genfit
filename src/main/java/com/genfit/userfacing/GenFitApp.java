package com.genfit.userfacing;

import com.genfit.database.AWSConnection;
import com.genfit.database.Database;

import java.util.HashMap;
import java.util.Map;

public class GenFitApp implements I32Application {
  private Map<String, ICommand> appCommands;
  private Database db;

  public GenFitApp() {

    this.appCommands = new HashMap<>();

    try {
      this.db = new Database(AWSConnection.getDBConnectionUsingIam());
    } catch (Exception e) {
      System.out.println("ERROR: error when instantiating Database");
    }

  }

  public Database getDb() {
    return this.db;
  }

  @Override
  public Map<String, ICommand> getAppCommands() {
    return appCommands;
  }

  @Override
  public boolean isCommandSupported(String cmd) {
    return appCommands.containsKey(cmd);
  }
}
