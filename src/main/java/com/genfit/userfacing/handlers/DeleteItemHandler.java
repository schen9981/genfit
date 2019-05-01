package com.genfit.userfacing.handlers;

import com.amazonaws.AmazonServiceException;
import com.genfit.database.S3Connection;
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

    String image = this.genFitApp.getDb().getItemBean(itemId).getImage();
    String imageKey = "";
    try {
      String[] urlSplit = image.split(S3Connection.getUrlPrefix());
      imageKey = urlSplit[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println(e.getMessage());
    }
    this.genFitApp.getDb().deleteItem(userId, itemId);
    String[] toReturn = new String[1];
    toReturn[0] = imageKey;
    return Main.GSON.toJson(toReturn);
  }
}
