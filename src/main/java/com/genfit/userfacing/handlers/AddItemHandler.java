package com.genfit.userfacing.handlers;

import java.util.Map;

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
import com.genfit.userfacing.Main;
import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddItemHandler implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {

    QueryParamsMap qm = request.queryMap();

    String[] itemInfoArr = new String[7];

    String name = qm.value("itemName");
    itemInfoArr[1] = name;

    String colorStr = qm.value("itemColor");
    itemInfoArr[2] = colorStr;
    int colorInt = (int) Long.parseLong(colorStr.replaceFirst("#", ""), 16);
    ColorAttribute color = new ColorAttribute(new Color(colorInt));

    String typeStr = qm.value("itemType1");
    itemInfoArr[3] = typeStr;
    TypeAttribute type1 = new TypeAttribute(
        Enum.valueOf(TypeEnum.class, typeStr));

    String patternStr = qm.value("itemPattern");
    itemInfoArr[4] = patternStr;
    PatternAttribute pattern = new PatternAttribute(
        Enum.valueOf(PatternEnum.class, patternStr));

    String seasonStr = qm.value("itemSeason");
    itemInfoArr[5] = seasonStr;
    SeasonAttribute season = new SeasonAttribute(
        Enum.valueOf(SeasonEnum.class, qm.value("itemSeason")));

    String formalityStr = qm.value("itemFormality");
    itemInfoArr[6] = formalityStr;
    FormalityAttribute formality = new FormalityAttribute(
        Enum.valueOf(FormalityEnum.class, formalityStr));

    // create new item to add
    int id = 0; // TODO: how to determine id of the item?
    itemInfoArr[0] = Integer.toString(id);
    Item newItem = new Item(id, name, season, formality, pattern, color, type1);

    // TODO: add item to database

    // return an item to add to html page

    Map<String, Object> variables = ImmutableMap.of("newItem", itemInfoArr);

    return Main.GSON.toJson(variables);
  }
}
