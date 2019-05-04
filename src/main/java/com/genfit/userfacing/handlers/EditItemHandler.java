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

public class EditItemHandler implements Route {

  private GenFitApp genFitApp;

  public EditItemHandler(GenFitApp genFitApp) {
    this.genFitApp = genFitApp;
  }

  @Override
  public String handle(Request request, Response response) throws Exception {

    QueryParamsMap qm = request.queryMap();
    String[] toReturn = new String[9];

    int itemId = Integer.parseInt(qm.value("itemId"));
    toReturn[0] = Integer.toString(itemId);
    System.out.println(toReturn[0]);

    String name = qm.value("itemName");
    toReturn[1] = name;
    System.out.println(toReturn[1]);

    String colorStr = qm.value("itemColor");
    toReturn[2] = colorStr.replaceFirst("#", "");
    int colorInt = (int) Long.parseLong(colorStr.replaceFirst("#", ""), 16);
    ColorAttribute color = new ColorAttribute(new Color(colorInt));
    System.out.println(toReturn[2]);

    String typeStr = qm.value("itemType1");
    toReturn[3] = typeStr;
    TypeAttribute type1 = new TypeAttribute(
        Enum.valueOf(TypeEnum.class, typeStr));
    System.out.println(toReturn[3]);

    String subTypeStr = qm.value("itemType2");
    toReturn[4] = subTypeStr;
    SubtypeAttribute type2 = new SubtypeAttribute(
        Enum.valueOf(SubtypeEnum.class, subTypeStr));
    System.out.println(toReturn[4]);

    String patternStr = qm.value("itemPattern");
    toReturn[5] = patternStr;
    PatternAttribute pattern = new PatternAttribute(
        Enum.valueOf(PatternEnum.class, patternStr));
    System.out.println(toReturn[5]);

    String seasonStr = qm.value("itemSeason");
    toReturn[6] = seasonStr;
    SeasonAttribute season = new SeasonAttribute(
        Enum.valueOf(SeasonEnum.class, seasonStr));
    System.out.println(toReturn[6]);

    String formalityStr = qm.value("itemFormality");
    toReturn[7] = formalityStr;
    FormalityAttribute formality = new FormalityAttribute(
        Enum.valueOf(FormalityEnum.class, formalityStr));
    System.out.println(toReturn[7]);

    // edit item
    this.genFitApp.getDb().editItem(itemId, type1, type2, formality, color,
        pattern, season);

    this.genFitApp.getDb().removeFromItemCache(itemId);

    toReturn[8] = this.genFitApp.getDb().getItemBean(itemId).getImage();

    // return an item to add to html page
    return Main.GSON.toJson(toReturn);
  }
}
