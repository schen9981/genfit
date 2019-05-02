package com.genfit.userfacing.handlers;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.proxy.ItemProxy;
import com.genfit.suggester.OutfitSuggester;
import com.genfit.suggester.OutfitSuggestion;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.LinkedList;
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
    List<OutfitSuggestion> completeOutfitsSuggestion =
            os.suggestOutfits(this.genFitApp.getDb(), id);

    List<JsonObject> completeOutfits = new LinkedList<>();
    List<JsonObject> incompleteOutfits = new LinkedList<>();
    for (OutfitSuggestion suggestion : completeOutfitsSuggestion) {
      Map<TypeEnum, ItemProxy> communityOutfitComp =
              suggestion.getCommunityOutfit().getItems();
      Map<TypeEnum, ItemProxy> userItems =
              suggestion.getSuggestedItemsForUser();

      JsonObject communityOutfitJson =
              this.convertItemsToJsonObj(communityOutfitComp);
      JsonObject userItemsJson =
              this.convertItemsToJsonObj(userItems);

      JsonObject suggestionJson = new JsonObject();

      suggestionJson.add("communityOutfit", communityOutfitJson);
      suggestionJson.add("userItems", userItemsJson);

      if (suggestion.isComplete()) {
        completeOutfits.add(suggestionJson);
      } else {
        List<ItemProxy> stillNeeded = suggestion.getItemsNeeded();
        JsonArray stillNeededJson = this.convertItemsToJsonArr(stillNeeded);
        suggestionJson.add("stillNeeded", stillNeededJson);
      }
    }

    Map<String, Object> output =
            ImmutableMap.of("complete", completeOutfits,
                    "incomplete", incompleteOutfits,
                    "likedOutfitIds", likedOutfitIds);

    return Main.GSON.toJson(output);
  }

  private JsonObject convertItemsToJsonObj(Map<TypeEnum, ItemProxy> itemMap) {
    JsonObject converted = new JsonObject();

    for (TypeEnum typeEnum : TypeEnum.values()) {
      ItemProxy item = itemMap.getOrDefault(typeEnum, null);

      if (item != null) {
        converted.addProperty(typeEnum.label, item.getId());
      }
    }

    return converted;
  }

  private JsonArray convertItemsToJsonArr(List<ItemProxy> itemList) {
    JsonArray converted = new JsonArray();

    for (ItemProxy itemProxy : itemList) {
      if (itemProxy != null) {
        converted.add(itemProxy.getId());
      }
    }

    return converted;
  }
}
