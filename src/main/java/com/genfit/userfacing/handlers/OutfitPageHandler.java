package com.genfit.userfacing.handlers;

import java.util.Map;

import com.genfit.userfacing.GenFitApp;
import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class OutfitPageHandler implements TemplateViewRoute {

  private GenFitApp genFitApp;

  public OutfitPageHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    String outfitJS = "<script src=\"js/outfit-page.js\"></script>";
    Map<String, Object> variables = ImmutableMap.of("title", "User Outfits",
        "additionalScripts", outfitJS);
    return new ModelAndView(variables, "outfitpage.ftl");
  }
}
