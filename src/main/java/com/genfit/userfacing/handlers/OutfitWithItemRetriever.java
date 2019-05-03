package com.genfit.userfacing.handlers;

import com.genfit.database.S3Connection;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutfitWithItemRetriever implements Route {

  private GenFitApp genFitApp;

  public OutfitWithItemRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) {

    QueryParamsMap qm = request.queryMap();
    int id = Integer.parseInt(qm.value("id"));
    List<Integer> outfitIds = new ArrayList<>();
    try {
      outfitIds = this.genFitApp.getDb().getOutfitsWithItemId(id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Map<String, Object> output = ImmutableMap.of("outfitIds", outfitIds);
    return Main.GSON.toJson(output);

  }
}

