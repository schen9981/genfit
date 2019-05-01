package com.genfit.userfacing;

import com.amazonaws.services.s3.AmazonS3;
import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.database.S3Connection;

import java.util.HashMap;
import java.util.Map;

public class GenFitApp implements I32Application {
  private Map<String, ICommand> appCommands;
  private Database db;
  private AmazonS3 s3;

  public GenFitApp() {

    this.appCommands = new HashMap<>();

    try {
      this.db = new Database(AWSConnection.getDBConnectionUsingIam());
      this.s3 = S3Connection.getS3Client();
    } catch (Exception e) {
      System.out.println("ERROR: error when instantiating Database");
    }

  }

  public Database getDb() {
    return this.db;
  }

  public AmazonS3 getS3() {
    return this.s3;
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
