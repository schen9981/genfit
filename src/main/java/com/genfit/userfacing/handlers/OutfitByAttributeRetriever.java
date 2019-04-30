package com.genfit.userfacing.handlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.proxy.ItemProxy;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class OutfitByAttributeRetriever implements Route {

  private GenFitApp genFitApp;

  public OutfitByAttributeRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    String username = qm.value("username");
    int userId = -1;
    try {
      userId = this.genFitApp.getDb().getUserBean(username).getId();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // get the requested type enum
    String component = qm.value("component");
    TypeEnum type = Enum.valueOf(TypeEnum.class, component);
    TypeAttribute typeAttr = new TypeAttribute(type);
    List<TypeAttribute> typeQueries = new ArrayList<>();
    typeQueries.add(typeAttr);

    Class typeToQuery = TypeAttribute.class;

    // get all items that are of a certain type
    List<ItemProxy> allItems = new ArrayList<>();
    try {
      allItems = this.genFitApp.getDb().getAllItemsByAttributes(typeAttr,
          typeQueries, userId);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // construct list of array representation of an item
    List<String[]> itemsByAttr = new ArrayList<>();
    for (ItemProxy p : allItems) {
      Item curr = p.getItem();
      itemsByAttr.add(UserItemRetriever.getItemInfoArr(curr));
    }

    Map<String, Object> variables = ImmutableMap.of("items", itemsByAttr);

    return Main.GSON.toJson(variables);
  }
}
