package com.genfit.userfacing.handlers;

import java.util.Map;

import com.genfit.userfacing.GenFitApp;
import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class AddItemHandler implements TemplateViewRoute {
  private GenFitApp genFitApp;

  public AddItemHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public ModelAndView handle(Request request, Response response)
      throws Exception {

    QueryParamsMap qm = request.queryMap();
    String name = qm.value("item-name");
    String color = qm.value("item-color");
    String type1 = qm.value("item-type-1");
    String type2 = qm.value("item-type-2");
    String pattern = qm.value("item-pattern");
    String season = qm.value("item-season");
    String formality = qm.value("item-formality");

    // TODO: create an item
    // TODO: add to database
    // TODO: update page html

    String itemJS = "<script src=\"js/item-page.js\"></script>";
    String itemCSS = "<link rel=\"stylesheet\" href=\"css/item-page.css\"></link>";
    Map<String, Object> variables = ImmutableMap.of("title", "User Items",
        "additionalScripts", itemJS, "additionalCSS", itemCSS);
    return new ModelAndView(variables, "itempage.ftl");
  }
}
