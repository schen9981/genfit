package com.genfit.suggester;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing an outfit suggestion that can already be completed by
 * the user.
 */
public class OutfitSuggestion {
  private OutfitProxy communityOutfit;
  private Map<TypeEnum, ItemProxy> suggestedOutfitMap;

  /**
   * Constructor for OutfitSuggestion.
   *
   * @param communityOutfit reference outfit for this suggestion
   */
  public OutfitSuggestion(OutfitProxy communityOutfit) {
    this.communityOutfit = communityOutfit;
    // placeholder ID
    this.suggestedOutfitMap = new HashMap<>();
  }

  /**
   * Method to check whether the outfit suggestion is for an outfit that a
   * user can already make.
   *
   * @return boolean flag indicating whether above is true
   */
  public boolean isComplete() {
    for (TypeEnum typeEnum : TypeEnum.values()) {
      if (this.suggestedOutfitMap.getOrDefault(typeEnum, null) == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the community item that the suggestion is based on.
   *
   * @return an OutfitProxy specifying the underlying outfit
   */
  public OutfitProxy getCommunityOutfit() {
    return this.communityOutfit;
  }

  /**
   * Returns the outfit composed of user's items that is being suggested.
   *
   * @return an OutfitProxy specifying the underlying outfit
   */
  public Map<TypeEnum, ItemProxy> getSuggestedItemsForUser() {
    return this.suggestedOutfitMap;
  }

  /**
   * Calculates the items that are needed by the user to complete this outfit
   * suggestion.
   *
   * @return a List containing the remaining items, will be empty if outfit
   * is complete
   */
  public List<ItemProxy> getItemsNeeded() {
    List<ItemProxy> itemsStillNeeded = new ArrayList<>();

    for (TypeEnum typeEnum : TypeEnum.values()) {
      if (this.suggestedOutfitMap.getOrDefault(typeEnum, null)
              == null) {
        ItemProxy stillNeededItem =
                this.getCommunityOutfit().getItems().get(typeEnum);
        if (stillNeededItem != null) {
          itemsStillNeeded.add(stillNeededItem);
        }
      }
    }

    return itemsStillNeeded;
  }

  /**
   * Adds the suggested item to the outfit.
   *
   * @param itemToSuggest item to be added to the suggested outfit
   */
  public void addSuggestedItem(ItemProxy itemToSuggest) {
    TypeEnum typeEnum = itemToSuggest.getTypeAttribute().getAttributeVal();
    this.suggestedOutfitMap.put(typeEnum, itemToSuggest);
  }
}
