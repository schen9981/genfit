package com.genfit.userfacing.handlers;

import java.util.Map;

import com.genfit.userfacing.GenFitApp;
import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class ItemPageHandler implements TemplateViewRoute {
  private GenFitApp genFitApp;

  public ItemPageHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    String itemJS = "<script src=\"js/item-page.js\"></script>";
    Map<String, Object> variables = ImmutableMap.of("title", "User Items",
        "additionalScripts", itemJS);
    return new ModelAndView(variables, "itempage.ftl");
  }
}
