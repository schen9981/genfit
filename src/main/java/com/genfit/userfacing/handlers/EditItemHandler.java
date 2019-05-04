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

//    username: username,
//    itemId: itemId,
//    itemName: $('#item-name').val(),
//    itemColor: $('#item-color').val(),
//    itemType1: $('#type-1').val(),
//    itemType2: $('#type-2').val(),
//    itemPattern: $('#item-pattern').val(),
//    itemSeason: $('#item-season').val(),
//    itemFormality: $('#item-formality').val()

    QueryParamsMap qm = request.queryMap();
    String[] toReturn = new String[9];

    int itemId = Integer.parseInt(qm.value("itemId"));
    toReturn[0] = Integer.toString(itemId);

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

    // get id of current user
    int id = this.genFitApp.getDb().getUserBean(qm.value("username")).getId();

    // edit item
    this.genFitApp.getDb().editItem(id, itemId, type1, type2, formality, color,
        pattern, season);

    toReturn[8] = this.genFitApp.getDb().getItemBean(itemId).getImage();

    // return an item to add to html page
    return Main.GSON.toJson(toReturn);
  }
}
