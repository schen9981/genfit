package com.genfit.suggester;

import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

import java.util.List;

/**
 * Interface for a suggested outfit.
 */
public interface OutfitSuggestion {
  /**
   * Method to check whether the outfit suggestion is for an outfit that a
   * user can already make.
   *
   * @return boolean flag indicating whether above is true
   */
  boolean isComplete();

  /**
   * Returns the underlying outfit that is being suggested.
   *
   * @return an OutfitProxy specifying the underlying outfit
   */
  OutfitProxy getFullOutfit();

  /**
   * Calculates the items that are needed by the user to complete this outfit
   * suggestion.
   *
   * @return a List containing the remaining items, will be empty if outfit
   * is complete
   */
  List<ItemProxy> getItemsNeeded();
}
