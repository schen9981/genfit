package com.genfit.userfacing.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.clothing.Item;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserItemRetriever implements Route {

  private GenFitApp genFitApp;

  public UserItemRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    String userId = qm.value("userId");

    // TODO: query database to get items of user
    List<Item> userItems = new ArrayList<>();

    Map<String, Object> variables = ImmutableMap.of("items", userItems);

    return Main.GSON.toJson(variables);
  }
}
