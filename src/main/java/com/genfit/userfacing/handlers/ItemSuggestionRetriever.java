package com.genfit.userfacing.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ItemSuggestionRetriever implements Route {
  private GenFitApp genFitApp;

  public ItemSuggestionRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {

    QueryParamsMap qm = req.queryMap();
    List<String[]> suggestionsInfo = new ArrayList<>();

    // get user id
    String username = qm.value("username");
    int userId = 0;
    try {
      userId = this.genFitApp.getDb().getUserBean(username).getId();
    } catch (Exception e) {
      Map<String, Object> output = ImmutableMap.of("suggestions",
          suggestionsInfo);
      return Main.GSON.toJson(output);
    }

    String outerId = qm.value("outer");
    String topId = qm.value("top");
    String bottomId = qm.value("bottom");
    String shoesId = qm.value("shoes");

    // create incomplete outfit
    Map<TypeEnum, ItemProxy> items = new HashMap<>();
    if (!outerId.equals("")) {
      items.put(TypeEnum.OUTER,
          new ItemProxy(this.genFitApp.getDb(), Integer.parseInt(outerId)));
    }

    if (!topId.equals("")) {
      items.put(TypeEnum.TOP,
          new ItemProxy(this.genFitApp.getDb(), Integer.parseInt(topId)));
    }

    if (!bottomId.equals("")) {
      items.put(TypeEnum.BOTTOM,
          new ItemProxy(this.genFitApp.getDb(), Integer.parseInt(bottomId)));
    }

    if (!shoesId.equals("")) {
      items.put(TypeEnum.SHOES,
          new ItemProxy(this.genFitApp.getDb(), Integer.parseInt(shoesId)));
    }
    Outfit incomplete = new Outfit(-1, "incomplete", items);

    // generate item suggestions
    OutfitSuggester os = new OutfitSuggester();
    List<ItemProxy> suggestions = os.suggestItems(incomplete,
        this.genFitApp.getDb(), userId);

    for (ItemProxy i : suggestions) {
      Item itemSuggest = i.getItem();
      String[] itemInfo = UserItemRetriever.getItemInfoArr(itemSuggest);
      suggestionsInfo.add(itemInfo);
    }

    Map<String, Object> output = ImmutableMap.of("suggestions",
        suggestionsInfo);

    return Main.GSON.toJson(output);
  }

}
