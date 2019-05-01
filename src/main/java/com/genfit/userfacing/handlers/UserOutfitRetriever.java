package com.genfit.userfacing.handlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserOutfitRetriever implements Route {

  private GenFitApp genFitApp;

  public UserOutfitRetriever(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  public static String[] getOutfitInfoArr(Outfit o,
                                          Map<TypeEnum, ItemProxy> outfitComp) {
    String[] outfitInfoArr = new String[6];

    outfitInfoArr[0] = Integer.toString(o.getId());
    outfitInfoArr[1] = o.getName();
    outfitInfoArr[2] = getIdIfExists(outfitComp, TypeEnum.OUTER);
    outfitInfoArr[3] = getIdIfExists(outfitComp, TypeEnum.TOP);
    outfitInfoArr[4] = getIdIfExists(outfitComp, TypeEnum.BOTTOM);
    outfitInfoArr[5] = getIdIfExists(outfitComp, TypeEnum.SHOES);

    return outfitInfoArr;
  }

  /**
   * Gets the id of the outfit component (item) if exists, -1 if doesn't.
   *
   * @param map - map of outfit components.
   * @param key - outfit component (type) to search for.
   * @return
   */
  public static String getIdIfExists(Map<TypeEnum, ItemProxy> map,
                                     TypeEnum key) {
    if (map.get(key) == null) {
      return Integer.toString(-1);
    } else {
      return Integer.toString(map.get(key).getItem().getId());
    }
  }

  @Override
  public String handle(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();

    String username = qm.value("username");

    List<OutfitProxy> userOutfitProxies = new ArrayList<>();
    List<String[]> userOutfits = new ArrayList<>();

    // get id for user
    int id;
    try {
      id = this.genFitApp.getDb().getUserBean(username).getId();
    } catch (Exception e1) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("outfits", userOutfits);
      return Main.GSON.toJson(variables);
    }

    // query database to get items of user
    try {
      userOutfitProxies = this.genFitApp.getDb().getOutfitsByUserID(id);
    } catch (SQLException e) {
      // if error, return empty list
      Map<String, Object> variables = ImmutableMap.of("items", userOutfits);
      return Main.GSON.toJson(variables);
    }

    for (OutfitProxy o : userOutfitProxies) {
      Map<TypeEnum, ItemProxy> outfitComp = o.getItems();
      Outfit currOut = o.getOutfit();

      String[] outfitInfoArr = getOutfitInfoArr(currOut, outfitComp);
      userOutfits.add(outfitInfoArr);
    }

    Map<String, Object> variables = ImmutableMap.of("outfits", userOutfits);
    return Main.GSON.toJson(variables);
  }
}
