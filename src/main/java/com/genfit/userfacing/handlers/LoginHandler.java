package com.genfit.userfacing.handlers;

import com.genfit.database.Database;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;
//import org.springframework.security.crypto.bcrypt.BCrypt;

public class LoginHandler implements Route {

  private static final Gson GSON = new Gson();
  private Database db;
  public LoginHandler(Database db) {
    this.db = db;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String username = qm.value("username");
    String hashPwd = qm.value("password");

    boolean correctInfo = false;
//    String finalHash = BCrypt.hashpw(hashPwd, BCrypt.gensalt());
    String finalHash = hashPwd;
//    System.out.println(finalHash);

    // Check database
    try {
      correctInfo = this.db.checkLogin(username, finalHash);
    } catch (Exception e) {
//      System.out.println("ERROR: Error when checking login info.");
      e.printStackTrace();
    }
    System.out.println(correctInfo);
    Map<String, Object> variables =
        ImmutableMap.of("success", correctInfo);
    return GSON.toJson(variables);
  }
}
