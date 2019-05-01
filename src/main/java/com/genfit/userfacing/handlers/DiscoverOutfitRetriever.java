package com.genfit.userfacing.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.suggester.OutfitSuggester;
import com.genfit.suggester.OutfitSuggestion;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class DiscoverOutfitRetriever implements Route {
  private GenFitApp genFitApp;

  public DiscoverOutfitRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    List<OutfitProxy> completeOutfits = new ArrayList<>();
    List<OutfitProxy> almostOutfits = new ArrayList<>();

    // get the current user id and the outfits the user liked
    String username = qm.value("username");
    List<Integer> likedOutfitIds = new ArrayList<>();
    int userId;
    try {
      userId = this.genFitApp.getDb().getUserBean(username).getId();
      likedOutfitIds = this.genFitApp.getDb().getLikedOutfitIds(userId);
    } catch (Exception e) {
      Map<String, Object> output = ImmutableMap.of("completeOutfits",
          new ArrayList<>(), "almostOutfits", new ArrayList<>(),
          "likedOutfitIds", new ArrayList<>());
      return Main.GSON.toJson(output);
    }

    // get complete and almost complete outfits from the discover algorithm
    OutfitSuggester os = new OutfitSuggester();
    List<OutfitSuggestion> outfitSugg = os
        .suggestOutfits(this.genFitApp.getDb(), userId);

    for (OutfitSuggestion o : outfitSugg) {
      // outfit is a complete outfit
      if (o.isComplete()) {
        Map<TypeEnum, ItemProxy> outfitItems = o.getSuggestedItemsForUser();
      }
    }

    Map<String, Object> output = ImmutableMap.of("completeOutfits",
        completeOutfits, "almostOutfits", almostOutfits, "likedOutfitIds",
        likedOutfitIds);

    return Main.GSON.toJson(output);
  }

}
