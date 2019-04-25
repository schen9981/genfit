package com.genfit.rankers;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.proxy.ItemProxy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Comparator for color attribute.
 */
public class ColorAttrRanker implements AttributeRanker<ColorAttribute> {
  private static Gson GSON = new Gson();
  private static String API_BASE_URL = "http://thecolorapi.com";

  /**
   * Queries API for a
   *
   * @param c
   * @return
   */
  private List<ColorAttribute> queryAPI(Color c, int numItems) {
    StringBuilder apiURL =
            new StringBuilder(API_BASE_URL);
    apiURL.append("/scheme?hex=");
    apiURL.append(c.toString());
    apiURL.append("&mode=analogic");
    apiURL.append("&count=");
    apiURL.append(numItems);

    List<ColorAttribute> matchingColors = new LinkedList<>();

    try {
      URL queryAPI = new URL(apiURL.toString());
      HttpURLConnection conn = (HttpURLConnection) queryAPI.openConnection();

      BufferedReader reader =
              new BufferedReader(new InputStreamReader(conn.getInputStream()));

      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      JsonObject jsonObj = GSON.fromJson(response.toString(),
              JsonObject.class);

      JsonElement numColors = jsonObj.get("count");
      numColors.getAsString();

      JsonArray colors = jsonObj.getAsJsonArray("colors");
      for (JsonElement colorElt : colors) {
        JsonObject colorObj = colorElt.getAsJsonObject();
        JsonObject rgbObj = colorObj.getAsJsonObject("rgb");
        int r = rgbObj.get("r").getAsInt();
        int g = rgbObj.get("g").getAsInt();
        int b = rgbObj.get("b").getAsInt();

        matchingColors.add(new ColorAttribute(
                new Color(Color.convertToHexVal(r, g, b))));
      }
    } catch (ClassCastException e) {
      System.out.println("INFO: ClassCast in ColorAttrRanker: "
              + apiURL.toString());
    } catch (MalformedURLException e) {
      System.out.println("INFO: Malformed URL in ColorAttrRanker: "
              + apiURL.toString());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("INFO: IOException in ColorAttrRanker");
    } finally {
      return matchingColors;
    }
  }

  @Override
  public List<ColorAttribute> rankByAttribute(List<ItemProxy> items) {
    List<ColorAttribute> matchingColors = new LinkedList<>();
    int numColorsPer = 0;

    // return empty list if no items
    if (items.size() > 0) {
      if (items.size() > 1) {
        numColorsPer = 1;
      } else {
        numColorsPer = 2;
      }

      for (ItemProxy item : items) {
        Color itemColor = item.getColorAttribute().getAttributeVal();
        List<ColorAttribute> apiMatchedColors = this.queryAPI(itemColor,
                numColorsPer);
        matchingColors.addAll(apiMatchedColors);
      }
    }

    return matchingColors;
  }
}
