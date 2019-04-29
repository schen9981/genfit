package com.genfit.userfacing.handlers;

import com.genfit.clothing.Item;
import com.genfit.userfacing.GenFitApp;

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

//    if (outerId != -1) {
//      Item outer = this.genFitApp.getDb().getItemBean(outerId);
//      String[] outerInfoAr = UserItemRetriever.getItemInfoArr(outer);
//    }
//
//    if (topId != -1) {
//      Item top = this.genFitApp.getDb().getItemBean(topId);
//      String[] topInfoArr = UserItemRetriever.getItemInfoArr(top);
//    }
//
//    if (bottomId != -1) {
//      Item bottom = this.genFitApp.getDb().getItemBean(bottomId);
//      String[] bottomInfoArr = UserItemRetriever.getItemInfoArr(bottom);
//    }
//
//    if (shoesId != -1) {
//      Item shoes = this.genFitApp.getDb().getItemBean(shoesId);
//      String[] shoesInfoArr = UserItemRetriever.getItemInfoArr(shoes);
//    }

//    Map<String, Object> variables = ImmutableMap.of("outfits", userOutfits);

//    return Main.GSON.toJson(variables);
    return null;
  }
}
