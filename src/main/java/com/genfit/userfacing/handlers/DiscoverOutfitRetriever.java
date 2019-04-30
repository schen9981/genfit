package com.genfit.userfacing.handlers;

import com.genfit.proxy.OutfitProxy;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscoverOutfitRetriever implements Route {
  private GenFitApp genFitApp;

  public DiscoverOutfitRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String username = qm.value("username");

    // TODO: Get outfits of two algorithm;
    List<OutfitProxy> completeOutfits = new ArrayList<>();
    List<OutfitProxy> almostOutfits = new ArrayList<>();
    Map<String, Object> output =
        ImmutableMap.of("completeOutfits", completeOutfits,
            "almostOutfits", almostOutfits);

    return Main.GSON.toJson(output);
  }

}
