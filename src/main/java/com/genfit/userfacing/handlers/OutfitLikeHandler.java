package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.SQLException;
import java.util.Map;

public class OutfitLikeHandler implements Route {
  private GenFitApp genFitApp;

  public OutfitLikeHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    int outfitId = Integer.parseInt(qm.value("outfitId"));
    System.out.println(outfitId);
    try {
      this.genFitApp.getDb().incrementLikes(outfitId);
    } catch (SQLException e) {
      System.out.println("ERROR: like handler error");
    }
    Map<String, Object> output =
        ImmutableMap.of("success", true);
    return Main.GSON.toJson(output);
  }

}