package com.genfit.userfacing.handlers;

import com.genfit.database.S3Connection;
import com.genfit.proxy.UserProxy;
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
    int itemId = Integer.parseInt(qm.value("itemId"));
    String username = qm.value("username");
    List<Integer> outfitIds = new ArrayList<>();
    int userId;
    try {
      userId = new UserProxy(this.genFitApp.getDb(), username).getId();
      outfitIds = this.genFitApp.getDb().getOutfitsWithItemId(userId, itemId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Map<String, Object> output = ImmutableMap.of("outfitIds", outfitIds);
    return Main.GSON.toJson(output);

  }
}

