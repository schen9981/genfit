package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class DeleteOutfitHandler implements Route {

  private GenFitApp genFitApp;

  public DeleteOutfitHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) throws Exception {

    QueryParamsMap qm = request.queryMap();

    String username = qm.value("username");
    int userId = this.genFitApp.getDb().getUserBean(username).getId();

    int outfitId = Integer.parseInt(qm.value("outfitId"));

    System.out.println(userId);
    System.out.println(outfitId);

    this.genFitApp.getDb().deleteOutfit(userId, outfitId);

    return Main.GSON.toJson("Successful Remove");
  }
}
