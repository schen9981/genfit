package com.genfit.userfacing.handlers;

import com.genfit.database.Database;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.Map;

public class UserformHandler implements Route {

  private static final Gson GSON = new Gson();
  private Database db;
  public UserformHandler(Database db) {
    this.db = db;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    int form = Integer.parseInt(qm.value("form"));
    String username = qm.value("username");

    // Log In
    if (form == 0) {
      String clientHashPwd = qm.value("password");
      boolean success = false;
      String name = "";
      // Check database
      try {
        success = this.db.checkLogin(username, clientHashPwd);
      } catch (Exception e) {
        // System.out.println("ERROR: Error when checking login info.");
        e.printStackTrace();
      }
      System.out.println(success);
      if (success) {
        try {
          name = this.db.getUserBean(username).getName();
        } catch (Exception e) {
          System.out.println("ERROR: Error when get user bean.");
        }
      }
      Map<String, Object> variables =
          ImmutableMap.of("success", success, "name", name);
      return GSON.toJson(variables);

    } else if (form == 1) {
      // Sign up
      String name = qm.value("name");
      String clientHashPwd = qm.value("password");
      boolean newUsername = true;
      String finalHash = BCrypt.hashpw(clientHashPwd, BCrypt.gensalt(10));
      // Check database
      try {
        newUsername = this.db.checkSignup(username);
      } catch (Exception e) {
        // System.out.println("ERROR: Error when checking signup info.");
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

    } else if (form == 2) {
      // Change Password
      String clientHashPwd = qm.value("password");
      String email = qm.value("username");
      String finalHash = BCrypt.hashpw(clientHashPwd, BCrypt.gensalt(10));
      boolean success = false;
      try {
        System.out.println(email);
        this.db.changePassword(email, finalHash);
        success = true;
      } catch (Exception e) {
        System.out.println("ERROR: error when changing password");
        e.printStackTrace();
      }
      Map<String, Object> variables =
          ImmutableMap.of("success", success);
      return GSON.toJson(variables);
    }
    return null;
  }
}
