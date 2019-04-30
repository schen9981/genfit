package com.genfit.userfacing.handlers;

import java.util.Map;

import com.genfit.clothing.Item;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class OutfitComponentsRetriever implements Route {

  private GenFitApp genFitApp;

  public OutfitComponentsRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    int outerId = Integer.parseInt(qm.value("outer"));
    int topId = Integer.parseInt(qm.value("top"));
    int bottomId = Integer.parseInt(qm.value("bottom"));
    int shoesId = Integer.parseInt(qm.value("shoes"));

    String[] outerInfoArr = new String[0];
    String[] topInfoArr = new String[0];
    String[] bottomInfoArr = new String[0];
    String[] shoesInfoArr = new String[0];

    try {
      if (outerId != -1) {
        Item outer = this.genFitApp.getDb().getItemBean(outerId);
        outerInfoArr = UserItemRetriever.getItemInfoArr(outer);
      }

      if (topId != -1) {
        Item top = this.genFitApp.getDb().getItemBean(topId);
        topInfoArr = UserItemRetriever.getItemInfoArr(top);
      }

      if (bottomId != -1) {
        Item bottom = this.genFitApp.getDb().getItemBean(bottomId);
        bottomInfoArr = UserItemRetriever.getItemInfoArr(bottom);
      }

      if (shoesId != -1) {
        Item shoes = this.genFitApp.getDb().getItemBean(shoesId);
        shoesInfoArr = UserItemRetriever.getItemInfoArr(shoes);
      }
    } catch (Exception e) {
      // TODO: better error handling
      System.out.println(e.getMessage());
    }

    Map<String, Object> variables = ImmutableMap.of("outer", outerInfoArr,
        "top", topInfoArr, "bottom", bottomInfoArr, "shoes", shoesInfoArr);

    return Main.GSON.toJson(variables);
  }
}
