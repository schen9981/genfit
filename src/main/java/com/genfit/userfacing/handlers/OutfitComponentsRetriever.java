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

    String[] outerInfoArr;
    if (outerId != -1) {
      Item outer;
      try {
        outer = this.genFitApp.getDb().getItemBean(outerId);
        outerInfoArr = UserItemRetriever.getItemInfoArr(outer);
      } catch (Exception e) {
        outerInfoArr = null;
      }
    } else {
      outerInfoArr = new String[0];
    }

    String[] topInfoArr;
    if (topId != -1) {
      Item top;
      try {
        top = this.genFitApp.getDb().getItemBean(topId);
        topInfoArr = UserItemRetriever.getItemInfoArr(top);
      } catch (Exception e) {
        topInfoArr = null;
      }
    } else {
      topInfoArr = new String[0];
    }

    String[] bottomInfoArr;
    if (bottomId != -1) {
      Item bottom;
      try {
        bottom = this.genFitApp.getDb().getItemBean(bottomId);
        bottomInfoArr = UserItemRetriever.getItemInfoArr(bottom);
      } catch (Exception e) {
        bottomInfoArr = null;
      }
    } else {
      bottomInfoArr = new String[0];
    }

    String[] shoesInfoArr;
    if (shoesId != -1) {
      Item shoes;
      try {
        shoes = this.genFitApp.getDb().getItemBean(shoesId);
        shoesInfoArr = UserItemRetriever.getItemInfoArr(shoes);
      } catch (Exception e) {
        shoesInfoArr = null;
      }
    } else {
      shoesInfoArr = new String[0];
    }

    Map<String, Object> variables = ImmutableMap.of("outer", outerInfoArr,
        "top", topInfoArr, "bottom", bottomInfoArr, "shoes", shoesInfoArr);

    return Main.GSON.toJson(variables);
  }
}
