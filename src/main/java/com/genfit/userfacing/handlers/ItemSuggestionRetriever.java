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

    // get user id
    String username = qm.value("username");
    int userId = 0;
    try {
      userId = this.genFitApp.getDb().getUserBean(username).getId();
    } catch (Exception e) {
      List<String[]> suggestionsInfo = new ArrayList<>();
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
    Map<TypeEnum, List<ItemProxy>> suggestions = os.suggestItems(incomplete,
        this.genFitApp.getDb(), userId);

    List<String[]> outerSuggestions = new ArrayList<>();
    if (suggestions.get(TypeEnum.OUTER) != null) {
      for (ItemProxy i : suggestions.get(TypeEnum.OUTER)) {
        Item itemSuggest = i.getItem();
        String[] itemInfo = UserItemRetriever.getItemInfoArr(itemSuggest);
        outerSuggestions.add(itemInfo);
      }
    }

    List<String[]> topSuggestions = new ArrayList<>();
    if (suggestions.get(TypeEnum.TOP) != null) {
      for (ItemProxy i : suggestions.get(TypeEnum.TOP)) {
        Item itemSuggest = i.getItem();
        String[] itemInfo = UserItemRetriever.getItemInfoArr(itemSuggest);
        topSuggestions.add(itemInfo);
      }
    }

    List<String[]> bottomSuggestions = new ArrayList<>();
    if (suggestions.get(TypeEnum.BOTTOM) != null) {
      for (ItemProxy i : suggestions.get(TypeEnum.BOTTOM)) {
        Item itemSuggest = i.getItem();
        String[] itemInfo = UserItemRetriever.getItemInfoArr(itemSuggest);
        bottomSuggestions.add(itemInfo);
      }
    }

    List<String[]> shoesSuggestions = new ArrayList<>();
    if (suggestions.get(TypeEnum.SHOES) != null) {
      for (ItemProxy i : suggestions.get(TypeEnum.SHOES)) {
        Item itemSuggest = i.getItem();
        String[] itemInfo = UserItemRetriever.getItemInfoArr(itemSuggest);
        shoesSuggestions.add(itemInfo);
      }
    }

    Map<String, Object> output = ImmutableMap.of("topSuggestions",
        topSuggestions, "outerSuggestions", outerSuggestions,
        "bottomSuggestions", bottomSuggestions, "shoesSuggestions",
        shoesSuggestions);

    return Main.GSON.toJson(output);
  }

}
