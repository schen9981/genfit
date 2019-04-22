package com.genfit.userfacing.handlers;

import com.genfit.userfacing.GenFitApp;
import com.google.common.collect.ImmutableMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.Map;

public class FrontPageHandler implements TemplateViewRoute {
  private GenFitApp genFitApp;

  public FrontPageHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Map<String, Object> variables = ImmutableMap.of("title", "Welcome to "
            + "GenFit!!!");
    return new ModelAndView(variables, "frontpage.ftl");
  }
}
