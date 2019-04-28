package com.genfit.userfacing.handlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.clothing.Item;
import com.genfit.proxy.ItemProxy;
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

    String username = qm.value("username");

    List<ItemProxy> userItemProxies = new ArrayList<>();
    List<Item> userItems = new ArrayList<>();

    // get id for user
    int id;
    try {
      id = this.genFitApp.getDb().getUserBean(username).getId();
      System.out.println(id);
    } catch (Exception e1) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("items", userItems);
      return Main.GSON.toJson(variables);
    }

    // query database to get items of user
    try {
      userItemProxies = this.genFitApp.getDb().getItemsByUserID(id);
      System.out.println(userItemProxies);
    } catch (SQLException e) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("items", userItems);
      return Main.GSON.toJson(variables);
    }

    for (ItemProxy p : userItemProxies) {
      userItems.add(p.getItem());
    }

    Map<String, Object> variables = ImmutableMap.of("items", userItems);

    return Main.GSON.toJson(variables);
  }
}
