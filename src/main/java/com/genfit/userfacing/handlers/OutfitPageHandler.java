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
    String outfitJS = "<script src=\"js/outfit-page.js\"></script><script src=\"js/suggest-outfits.js\"></script>";
    String outfitCSS = "<link rel=\"stylesheet\" href=\"css/outfit-page.css\"></link>";
    Map<String, Object> variables = ImmutableMap.of("title", "User Outfits",
        "additionalScripts", outfitJS, "additionalCSS", outfitCSS);
    return new ModelAndView(variables, "outfitpage.ftl");
  }
}
