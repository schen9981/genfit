package com.genfit.userfacing.handlers;

import com.genfit.database.Database;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import jdk.nashorn.internal.ir.annotations.Immutable;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.Map;

public class SignupHandler implements Route {

  private static final Gson GSON = new Gson();
  private Database db;
  public SignupHandler(Database db) {
    this.db = db;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String name = qm.value("name");
    String username = qm.value("username");
    String clientHashPwd = qm.value("password");

    boolean newUsername = true;
    String finalHash = BCrypt.hashpw(clientHashPwd, BCrypt.gensalt(10));
//    String finalHash = hashPwd;

    System.out.println(BCrypt.checkpw(clientHashPwd, finalHash) + "$$");
    // Check database
    try {
      newUsername = this.db.checkSignup(username);
    } catch (Exception e) {
//      System.out.println("ERROR: Error when checking signup info.");
      e.printStackTrace();
    }

    if (newUsername) {
      // add to database
      try {
        this.db.addUser(name, username, finalHash);
      } catch (SQLException e) {
        System.out.println("ERROR: error when adding new user to database.");
      }
    }

    Map<String, Object> variables = ImmutableMap.of("success", newUsername);
    return GSON.toJson(variables);
  }
}
