package com.genfit.userfacing.handlers;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.suggester.OutfitSuggester;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.Map;

public class SingleItemRetriever implements Route {

  private GenFitApp genFitApp;

  public SingleItemRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    int id = Integer.parseInt(qm.value("id"));
    Item item = new ItemProxy(this.genFitApp.getDb(), id).getItem();
    String[] itemInfoArr = new String[9];
    itemInfoArr[0] = Integer.toString(item.getId());
    itemInfoArr[1] = item.getName();
    itemInfoArr[2] = item.getColor().getAttributeVal().toString();
    itemInfoArr[3] = item.getType().getAttributeVal().toString();
    itemInfoArr[4] = item.getSubtype().getAttributeVal().toString();
    itemInfoArr[5] = item.getPattern().getAttributeVal().toString();
    itemInfoArr[6] = item.getSeason().getAttributeVal().toString();
    itemInfoArr[7] = item.getFormality().getAttributeVal().toString();
    itemInfoArr[8] = item.getImage();
    Map<String, Object> variables = ImmutableMap.of("item", itemInfoArr);
    return Main.GSON.toJson(variables);
  }

}
