package com.genfit.userfacing.handlers;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.SubtypeAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.SubtypeEnum;
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
    String[] toReturn = new String[9];

    String name = qm.value("itemName");
    toReturn[1] = name;

    String colorStr = qm.value("itemColor");
    toReturn[2] = colorStr;
    int colorInt = (int) Long.parseLong(colorStr.replaceFirst("#", ""), 16);
    ColorAttribute color = new ColorAttribute(new Color(colorInt));

    String typeStr = qm.value("itemType1");
    toReturn[3] = typeStr;
    TypeAttribute type1 = new TypeAttribute(
        Enum.valueOf(TypeEnum.class, typeStr));

    String subTypeStr = qm.value("itemType2");
    toReturn[4] = subTypeStr;
    SubtypeAttribute type2 = new SubtypeAttribute(
        Enum.valueOf(SubtypeEnum.class, subTypeStr));

    String patternStr = qm.value("itemPattern");
    toReturn[5] = patternStr;
    PatternAttribute pattern = new PatternAttribute(
        Enum.valueOf(PatternEnum.class, patternStr));

    String seasonStr = qm.value("itemSeason");
    toReturn[6] = seasonStr;
    SeasonAttribute season = new SeasonAttribute(
        Enum.valueOf(SeasonEnum.class, seasonStr));

    String formalityStr = qm.value("itemFormality");
    toReturn[7] = formalityStr;
    FormalityAttribute formality = new FormalityAttribute(
        Enum.valueOf(FormalityEnum.class, formalityStr));

    String imageKey = qm.value("imageKey");

    // get id of current user
    int id = this.genFitApp.getDb().getUserBean(qm.value("username")).getId();
    // add item
    int itemId = this.genFitApp.getDb().addItem(id, name, type1, formality,
        color, pattern, season, imageKey, type2);

    toReturn[0] = Integer.toString(itemId);
    toReturn[8] = this.genFitApp.getDb().getItemBean(itemId).getImage();

    // return an item to add to html page
    return Main.GSON.toJson(toReturn);
  }
}
