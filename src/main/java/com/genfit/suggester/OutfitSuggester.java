package com.genfit.suggester;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.ItemDistanceCalculator;
import com.genfit.clothing.Outfit;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

/**
 * Utility class that suggests new outfits based on suggested attributes.
 */
public class OutfitSuggester {

  /**
   * Private constructor for utility class.
   */
  public OutfitSuggester() {
  }

  /**
   * Method that determines which class contains the minimum number of
   * attributes.
   *
   * @param attr - map of attributes
   * @return minimum class
   */
  private static Class minAttrToQuery(
          Map<Class, List<? extends Attribute>> attr) {
    Class minClass = null;
    int minSize = Integer.MAX_VALUE;
    for (Class c : attr.keySet()) {
      List<? extends Attribute> attrList = attr.get(c);
      int size;
      if (attrList != null) {
        Attribute a = attrList.get(0);
        // don't allow color attribute to get matched
        if (!a.getAttributeName()
                .equals(new ColorAttribute(null).getAttributeName())) {
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

  /**
   * Method that gets the outfits to suggest based on the list of attributes
   * generated from the attribute suggestor.
   *
   * @param outfit partially specified outfit to use as base for suggestions
   * @param db     database to pull suggestions from
   * @return list of items suggested based on incomplete outfit
   */
  public Map<TypeEnum, List<ItemProxy>> suggestItems(Outfit outfit, Database db,
                                                     int userID) {
    List<ItemProxy> suggestions = new ArrayList<>();

    Map<Class, List<? extends Attribute>> outfitAttr = AttributeSuggester
            .getMatchingOutfitAttributes(outfit);

    List<Integer> itemIDsToFilter = new LinkedList<>();
    for (ItemProxy item : outfit.getItems().values()) {
      itemIDsToFilter.add(item.getId());
    }

    // get the class that has the minimum number of attributes to query
    Class classToQuery = minAttrToQuery(outfitAttr);

    // create set of all other classes to query on (exclude min)
    Map<Class, List<? extends Attribute>> otherClasses = new HashMap<>(
            outfitAttr);
    otherClasses.remove(classToQuery);
    otherClasses.remove(ColorAttribute.class);

    List<? extends Attribute> attrVals = outfitAttr.get(classToQuery);

    if (attrVals != null && attrVals.size() > 0) {
      // get list of items that have matching attributes of smallest query
      try {
        List<ItemProxy> eligible = db.getAllItemsByAttributes(attrVals.get(0),
                attrVals, userID);

        suggestions = this.filterByAttribute(eligible, otherClasses,
                itemIDsToFilter);
      } catch (SQLException e) {
        System.out.println(
                "ERROR: SQL exception when querying for outfit " +
                        "suggestions");
      }
    }

    return this.sortItemsIntoTypes(suggestions);
  }

  private List<ItemProxy> filterByAttribute(List<ItemProxy> originals,
                                            Map<Class, List<?
                                                    extends Attribute>> otherClassVals,
                                            List<Integer> itemIDsToFilter) {
    List<ItemProxy> filtered = new ArrayList<>();
    for (int i = 0; i < originals.size(); i++) {
      ItemProxy itemProxy = originals.get(i);
      boolean shouldAdd = true;
      for (Class otherClass : otherClassVals.keySet()) {
        List<? extends Attribute> otherAttrVals = otherClassVals
                .get(otherClass);
        if (!otherAttrVals
                .contains(itemProxy.getAttributeForItem(otherClass))) {
          shouldAdd = false;
          break;
        }
      }
      if (shouldAdd && !itemIDsToFilter.contains(itemProxy.getId())) {
        filtered.add(itemProxy);
      }
    }

    return filtered;
  }

  public List<OutfitSuggestion> suggestOutfits(Database db, int userID) {
    List<OutfitSuggestion> outfitSuggestions = new ArrayList<>();

    try {
      List<ItemProxy> userItems = db.getItemsByUserID(userID);
      List<OutfitProxy> allOtherOutfits = db.getOutfitsExcludeUser(userID);
      // shuffle order of outfits found from database
      Collections.shuffle(allOtherOutfits, new Random());

      // sort user item by type of item
      Map<TypeEnum, List<ItemProxy>> userItemsSortedByType = this
              .sortItemsIntoTypes(userItems);

      // iterate through all community outfits
      for (OutfitProxy communityOutfit : allOtherOutfits) {
        // get community items
        Map<TypeEnum, ItemProxy> itemsInCommunityOutfit = communityOutfit
                .getItems();

        int numUserItemsMatched = 0;
        int numItemsInCommunityOutfit = 0;
        OutfitSuggestion suggestion = new OutfitSuggestion(communityOutfit);

        // iterate through types of clothing in each community outfit
        for (TypeEnum typeEnum : TypeEnum.values()) {
          ItemProxy communityItemOfType = itemsInCommunityOutfit
                  .getOrDefault(typeEnum, null);

          if (communityItemOfType != null) {
            numItemsInCommunityOutfit++;
            // user items matching this type
            List<ItemProxy> userItemsOfType = userItemsSortedByType
                    .getOrDefault(typeEnum, Collections.emptyList());
            // shuffle order of user items presented
            Collections.shuffle(userItemsOfType, new Random());

            // loop through user items of that type to find distance
            for (ItemProxy userItemOfType : userItemsOfType) {
              if (ItemDistanceCalculator.areSimilar(userItemOfType,
                      communityItemOfType)) {
                suggestion.addSuggestedItem(userItemOfType);
                numUserItemsMatched++;
                // recommend the first user item of that type that was found
                // to meet the threshold
                break;
              }
            }
          }
        }

        if ((numItemsInCommunityOutfit - numUserItemsMatched) <= 1) {
          outfitSuggestions.add(suggestion);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return outfitSuggestions;
  }

  private Map<TypeEnum, List<ItemProxy>> sortItemsIntoTypes(
          List<ItemProxy> items) {
    Map<TypeEnum, List<ItemProxy>> sortedItems = new HashMap<>();
    for (int i = 0; i < items.size(); i++) {
      ItemProxy itemProxy = items.get(i);
      List<ItemProxy> itemList = new ArrayList<>();
      itemList.add(itemProxy);
      sortedItems.merge(itemProxy.getTypeAttribute().getAttributeVal(),
              itemList, (oldList, newList) -> {
                oldList.addAll(newList);
                return oldList;
              });
    }
    return sortedItems;
  }
}
