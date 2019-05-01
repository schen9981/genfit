package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.Map;

public class OutfitLikeHandler implements Route {
  private GenFitApp genFitApp;

  public OutfitLikeHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String username = qm.value("username");
    int mode = Integer.parseInt(qm.value("mode"));
    int outfitId = Integer.parseInt(qm.value("outfitId"));
    int id;
//    System.out.println(outfitId);
    int likes = 0;
    try {
      id = this.genFitApp.getDb().getUserBean(username).getId();
      if (mode != 0) {
        this.genFitApp.getDb().changeLikes(outfitId, id, mode);
      }
      likes = this.genFitApp.getDb().getOutfitLikes(outfitId);
    } catch (Exception e) {
//      System.out.println("ERROR: like handler error");
      e.printStackTrace();
    }

    Map<String, Object> output =
        ImmutableMap.of("success", true, "likes", likes);
    return Main.GSON.toJson(output);
  }

}