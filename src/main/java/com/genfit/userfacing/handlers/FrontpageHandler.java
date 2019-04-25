package com.genfit.userfacing.handlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class FrontpageHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {
    Map<String, Object> variables = ImmutableMap.of("title", "Welcome to GenFit");
    return new ModelAndView(variables, "frontpage.ftl");
  }
}
