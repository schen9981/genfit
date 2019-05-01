package com.genfit.userfacing.handlers;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscoverOutfitRetriever implements Route {
  private GenFitApp genFitApp;

  public DiscoverOutfitRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String username = qm.value("username");
    int id;
    List<Integer> likedOutfitIds = new ArrayList<>();

    try {
      id = this.genFitApp.getDb().getUserBean(username).getId();
      likedOutfitIds = this.genFitApp.getDb().getLikedOutfitIds(id);
    } catch (Exception e) {
      Map<String, Object> output =
          ImmutableMap.of("completeOutfits", new ArrayList<>(),
              "almostOutfits", new ArrayList<>(),
              "likedOutfitIds", new ArrayList<>());
      return Main.GSON.toJson(output);
    }

    OutfitSuggester os = new OutfitSuggester();
    List<OutfitSuggestion> completeOutfitsSuggestion = os.suggestOutfits(this.genFitApp.getDb(), id);
//    System.out.println(completeOutfitsSuggestion);

    List<String[]> completeOutfits = new ArrayList<>();
    List<String[]> almostOutfits = new ArrayList<>();
    for (OutfitSuggestion suggestion : completeOutfitsSuggestion) {
      Map<TypeEnum, ItemProxy> outfitComp = suggestion.getCommunityOutfit().getItems();
      Outfit currOut = suggestion.getCommunityOutfit().getOutfit();
      String[] outfitInfoArr = UserOutfitRetriever.getOutfitInfoArr(currOut, outfitComp);
      if (suggestion.isComplete()) {
        completeOutfits.add(outfitInfoArr);
      } else {
        almostOutfits.add(outfitInfoArr);
      }
    }

    Map<String, Object> output =
        ImmutableMap.of("completeOutfits", completeOutfits,
            "almostOutfits", almostOutfits,
            "likedOutfitIds", likedOutfitIds);

    return Main.GSON.toJson(output);
  }

}
