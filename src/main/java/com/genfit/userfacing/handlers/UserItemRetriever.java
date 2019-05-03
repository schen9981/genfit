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

  /**
   * Gets the array representation of an item.
   *
   * @param item - item we are extracting information from.
   * @return array representation
   */
  public static String[] getItemInfoArr(Item item) {
    String[] itemInfoArr = new String[9];
    itemInfoArr[0] = Integer.toString(item.getId());
    itemInfoArr[1] = item.getName();
    itemInfoArr[2] = item.getColor().getAttributeVal().toString();
    itemInfoArr[3] = item.getType().getAttributeVal().toString();
    itemInfoArr[4] = item.getSubtype().getAttributeVal().toString();
    itemInfoArr[5] = item.getPattern().getAttributeVal().toString();
    itemInfoArr[6] = item.getSeason().getAttributeVal().toString();
    itemInfoArr[7] = item.getFormality().getAttributeVal().toString();
    itemInfoArr[8] = item.getImage();

    return itemInfoArr;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    String username = qm.value("username");

    List<ItemProxy> userItemProxies = new ArrayList<>();
    List<String[]> userItems = new ArrayList<>();

    // get id for user
    int id;
    try {
      id = this.genFitApp.getDb().getUserBean(username).getId();
    } catch (Exception e1) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("items", userItems);
      return Main.GSON.toJson(variables);
    }

    // query database to get items of user
    try {
      userItemProxies = this.genFitApp.getDb().getItemsByUserID(id);
    } catch (SQLException e) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("items", userItems);
      return Main.GSON.toJson(variables);
    }

    for (ItemProxy p : userItemProxies) {
      Item currItem = p.getItem();
      String[] itemInfoArr = getItemInfoArr(currItem);
      userItems.add(itemInfoArr);
    }

    Map<String, Object> variables = ImmutableMap.of("items", userItems);

    return Main.GSON.toJson(variables);
  }

}
