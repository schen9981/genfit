package com.genfit.userfacing.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddOutfitHandler implements Route {

  private GenFitApp genFitApp;

  public AddOutfitHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) throws Exception {

    // outfit: [id, name, outer, top, bottom, shoes]

    QueryParamsMap qm = request.queryMap();
    String[] toReturn = new String[6];

    String outfitName = qm.value("name");
    toReturn[1] = outfitName;

    int outerId = Integer.parseInt(qm.value("outer"));
    toReturn[2] = Integer.toString(outerId);

    int topId = Integer.parseInt(qm.value("top"));
    toReturn[3] = Integer.toString(topId);

    int bottomId = Integer.parseInt(qm.value("bottom"));
    toReturn[4] = Integer.toString(bottomId);

    int shoesId = Integer.parseInt(qm.value("shoes"));
    toReturn[5] = Integer.toString(shoesId);

    Map<TypeEnum, Integer> components = new HashMap<>();
    components.put(TypeEnum.OUTER, outerId);
    components.put(TypeEnum.TOP, topId);
    components.put(TypeEnum.BOTTOM, bottomId);
    components.put(TypeEnum.SHOES, shoesId);

    // get id of current user
    int userId = this.genFitApp.getDb().getUserBean(qm.value("username"))
            .getId();

    // add item
    int outfitId = this.genFitApp.getDb().addOutfit(userId, outfitName,
            components);
    toReturn[0] = Integer.toString(outfitId);

    // return an outfit to add to html page
    return Main.GSON.toJson(toReturn);
  }
}
