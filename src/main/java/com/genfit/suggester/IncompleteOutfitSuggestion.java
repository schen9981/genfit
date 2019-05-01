package com.genfit.suggester;

import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

import java.util.List;

/**
 * Class representing an outfit suggestion that still has to be completed.
 */
public class IncompleteOutfitSuggestion implements OutfitSuggestion {
  private OutfitProxy underlyingOutfit;
  private List<ItemProxy> remainingItems;

  public IncompleteOutfitSuggestion(OutfitProxy underlyingOutfit,
                                    List<ItemProxy> remainingItems) {
    this.underlyingOutfit = underlyingOutfit;
    this.remainingItems = remainingItems;
  }

  @Override
  public boolean isComplete() {
    return false;
  }

  @Override
  public OutfitProxy getFullOutfit() {
    return this.underlyingOutfit;
  }

  @Override
  public List<ItemProxy> getItemsNeeded() {
    return this.remainingItems;
  }
}
