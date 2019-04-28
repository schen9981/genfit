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
    String[] toReturn = new String[7];

    String name = qm.value("itemName");
    toReturn[0] = name;

    String colorStr = qm.value("itemColor");
    toReturn[1] = colorStr;
    int colorInt = (int) Long.parseLong(colorStr.replaceFirst("#", ""), 16);
    ColorAttribute color = new ColorAttribute(new Color(colorInt));

    String typeStr = qm.value("itemType1");
    toReturn[2] = typeStr;
    TypeAttribute type1 = new TypeAttribute(
        Enum.valueOf(TypeEnum.class, typeStr));

    String patternStr = qm.value("itemPattern");
    toReturn[3] = patternStr;
    PatternAttribute pattern = new PatternAttribute(
        Enum.valueOf(PatternEnum.class, patternStr));

    String seasonStr = qm.value("itemSeason");
    toReturn[4] = seasonStr;
    SeasonAttribute season = new SeasonAttribute(
        Enum.valueOf(SeasonEnum.class, seasonStr));

    String formalityStr = qm.value("itemFormality");
    toReturn[5] = formalityStr;
    FormalityAttribute formality = new FormalityAttribute(
        Enum.valueOf(FormalityEnum.class, formalityStr));

    // get id of current user
    int id = this.genFitApp.getDb().getUserBean(qm.value("username")).getId();
    // add item
    this.genFitApp.getDb().addItem(id, name, type1, formality, color, pattern,
        season);

    // return an item to add to html page
    return Main.GSON.toJson(toReturn);
  }
}
