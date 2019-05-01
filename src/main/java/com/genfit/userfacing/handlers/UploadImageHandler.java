package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

/**
 * Created by ericwang on 2019/4/29.
 */
public class UploadImageHandler implements Route {
  private GenFitApp genFitApp;

  public UploadImageHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    System.out.println("reached handler");
    QueryParamsMap qm = req.queryMap();
    for (String s : qm.values()) {
      System.out.println(s);
    }
    System.out.println();
    String data = qm.value("imageData");
    System.out.println(data);

    Map<String, Object> variables = ImmutableMap.of("items", "");

    return Main.GSON.toJson(variables);
  }

}
