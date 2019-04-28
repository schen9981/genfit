package com.genfit.userfacing.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.clothing.Outfit;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserOutfitRetriever implements Route {

  private GenFitApp genFitApp;

  public UserOutfitRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    String userId = qm.value("userId");

    // TODO: get information for each outfit, parse into json
    List<Outfit> userOutfits = new ArrayList<>();

    Map<String, Object> variables = ImmutableMap.of("outfits", userOutfits);

    return Main.GSON.toJson(variables);
  }
}
