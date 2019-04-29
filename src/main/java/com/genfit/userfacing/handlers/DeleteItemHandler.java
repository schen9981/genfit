package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class DeleteItemHandler implements Route {

  private GenFitApp genFitApp;

  public DeleteItemHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) throws Exception {

    QueryParamsMap qm = request.queryMap();

    String username = qm.value("username");
    int userId = this.genFitApp.getDb().getUserBean(username).getId();

    int itemId = Integer.parseInt(qm.value("itemId"));

    this.genFitApp.getDb().deleteItem(userId, itemId);

    return Main.GSON.toJson("Successful Remove");
  }
}
