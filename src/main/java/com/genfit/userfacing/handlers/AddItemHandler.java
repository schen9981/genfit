package com.genfit.userfacing.handlers;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.userfacing.GenFitApp;
import com.genfit.userfacing.Main;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddItemHandler implements Route {

  private GenFitApp genFitApp;

  public AddItemHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) throws Exception {

    QueryParamsMap qm = request.queryMap();

    String name = qm.value("itemName");

    String colorStr = qm.value("itemColor");
    int colorInt = (int) Long.parseLong(colorStr.replaceFirst("#", ""), 16);
    ColorAttribute color = new ColorAttribute(new Color(colorInt));

    String typeStr = qm.value("itemType1");
    TypeAttribute type1 = new TypeAttribute(
        Enum.valueOf(TypeEnum.class, typeStr));

    String patternStr = qm.value("itemPattern");
    PatternAttribute pattern = new PatternAttribute(
        Enum.valueOf(PatternEnum.class, patternStr));

    String seasonStr = qm.value("itemSeason");
    SeasonAttribute season = new SeasonAttribute(
        Enum.valueOf(SeasonEnum.class, seasonStr));

    String formalityStr = qm.value("itemFormality");
    FormalityAttribute formality = new FormalityAttribute(
        Enum.valueOf(FormalityEnum.class, formalityStr));

    // create new item to add
    int id = 0; // TODO: how to determine id of the item?
    Item newItem = new Item(id, name, season, formality, pattern, color, type1);

    // TODO: add item to database

    // return an item to add to html page
    System.out.println(Main.GSON.toJson(newItem));

    return Main.GSON.toJson(newItem);
  }
}
