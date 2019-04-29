package com.genfit.suggester;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.clothing.Outfit;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;

/**
 * Utility class that suggests new outfits based on suggested attributes.
 */
public class OutfitSuggester {

  /**
   * Private constructor for utility class.
   */
  public OutfitSuggester() {
  }

  private List<Outfit> filterByAttributes() {
    return new ArrayList<>();
  }

  /**
   * Method that gets the outfits to suggest based on the list of attributes
   * generated from the attribute suggestor.
   *
   * @param outfit partially specified outfit to use as base for suggestions
   * @param db     database to pull suggestions from
   * @return list of outfits to suggest
   */
  public List<Outfit> suggestOutfits(Outfit outfit,
                                     Database db,
                                     int userID) {
    List<Outfit> suggestions = new ArrayList<>();

    Map<Class, List<? extends Attribute>> outfitAttr =
            AttributeSuggester.getMatchingOutfitAttributes(outfit);

    // get the class that has the minimum number of attributes to query
    Class classToQuery = minAttrToQuery(outfitAttr);

    // create set of all other classes to query on (exclude min)
    Set<Class> otherClasses = new HashSet<>();
    otherClasses.addAll(outfitAttr.keySet());
    otherClasses.remove(classToQuery);

    List<? extends Attribute> attrVals = outfitAttr.get(classToQuery);

    System.out.println("classToQuery" + classToQuery);

    if (attrVals != null && attrVals.size() > 0) {
      // get list of items that have matching attributes of smallest query
      try {
        List<ItemProxy> eligible = db.getAllItemsByAttributes(attrVals.get(0),
                attrVals);

        System.out.println(eligible.size());

        for (int i = 0; i < eligible.size(); i++) {
          ItemProxy itemProxy = eligible.get(i);
        }
      } catch (SQLException e) {
        System.out.println("ERROR: SQL exception when querying for outfit "
                + "suggestions");
      }

      // sort through for the other attributes
      // TODO: write method for sorting through other attributes.
    }

    return suggestions;
  }

  /**
   * Method that determines which class contains the minimum number of
   * attributes.
   *
   * @param attr - map of attributes
   * @return minimum class
   */
  private static Class minAttrToQuery(Map<Class, List<? extends Attribute>> attr) {
    Class minClass = null;
    int minSize = Integer.MAX_VALUE;
    for (Class c : attr.keySet()) {
      List<? extends Attribute> attrList = attr.get(c);
      int size;
      if (attrList != null) {
        Attribute a = attrList.get(0);
        // don't allow color attribute to get matched
        if (!a.getAttributeName().equals(new ColorAttribute(null).getAttributeName())) {
          size = attrList.size();
          if (size < minSize) {
            minSize = size;
            minClass = c;
          }
        }
      }
    }
    return minClass;
  }
}
