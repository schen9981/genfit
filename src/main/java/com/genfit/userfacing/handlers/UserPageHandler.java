package com.genfit.userfacing.handlers;

import java.util.Map;

import com.genfit.userfacing.GenFitApp;
import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class UserPageHandler implements TemplateViewRoute {
  private GenFitApp genFitApp;

  public UserPageHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    Map<String, Object> variables = ImmutableMap.of("title", "GenFit");
    return new ModelAndView(variables, "userpage.ftl");
  }
}
