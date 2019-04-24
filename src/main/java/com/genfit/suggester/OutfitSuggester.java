package com.genfit.suggester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.genfit.attribute.Attribute;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.database.Database;

/**
 * Utility class that suggests new outfits based on suggested attributes.
 */
public class OutfitSuggester {

  /**
   * Private constructor for utility class.
   */
  private OutfitSuggester() {
  }

  /**
   * Method that gets the outfits to suggest based on the list of attributes
   * generated from the attribute suggestor.
   *
   * @param attr TODO: fill this in
   * @param db
   * @return list of outfits to suggest
   */
  public List<Outfit> suggestOutfits(Map<Class, List<Attribute>> attr,
                                     Database db) {

    // get the class that has the minimum number of attributes to query
    Class classToQuery = minAttrToQuery(attr);

    // create set of all other classes to query on (exclude min)
    Set<Class> otherClasses = attr.keySet();
    otherClasses.remove(classToQuery);

    // get list of items that have matching attributes of smallest query

    // TODO: figure out this DBQuery
    //List<Item> match = db.getAllItemsByAttributes(attr);

    // sort through for the other attributes
    // TODO: write method for sorting through other attributes.

    List<Outfit> suggestions = new ArrayList<>();
    return suggestions;
  }

  /**
   * Method that determines which class contains the minimum number of
   * attributes.
   *
   * @param attr - map of attributes
   * @return minimum class
   */
  private static Class minAttrToQuery(Map<Class, List<Attribute>> attr) {
    Class minClass = null;
    int minSize = Integer.MAX_VALUE;
    for (Class c : attr.keySet()) {
      int size = attr.get(c).size();
      if (size < minSize) {
        minSize = size;
        minClass = c;
      }
    }
    return minClass;
  }

}
