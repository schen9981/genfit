package com.genfit.userfacing.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;

import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.AttributeEnum;
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
    int userId = this.genFitApp.getDb().getUserBean(username).getId();

    // get the requested type enum
    String component = qm.value("component");
    TypeEnum type = Enum.valueOf(TypeEnum.class, component);
    List<Attribute> toMatch = new ArrayList<>();
    toMatch.add(new TypeAttribute(type));


    // get all items that are of a certain type
    List<ItemProxy> allItems = this.genFitApp.getDb()
        .getAllItemsByAttributes(AttributeEnum.TYPE, toMatch);

    // construct list of array representation of an item
    List<String[]> itemsByAttr = new ArrayList<>();
    for (ItemProxy p : allItems) {
      Item curr = p.getItem();
      itemsByAttr.add(UserItemRetriever.getItemInfoArr(curr));
    }

    Map<String, Object> variables = ImmutableMap.of("items" itemsByAttr);

    return Main.GSON.toJson(variables);
  }
}
