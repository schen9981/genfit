package com.genfit.suggester;

import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an outfit suggestion that can already be completed by
 * the user.
 */
public class CompleteOutfitSuggestion implements OutfitSuggestion {
  private OutfitProxy underlyingOutfit;

  public CompleteOutfitSuggestion(OutfitProxy underlyingOutfit) {
    this.underlyingOutfit = underlyingOutfit;
  }

  @Override
  public boolean isComplete() {
    return true;
  }

  @Override
  public OutfitProxy getFullOutfit() {
    return this.underlyingOutfit;
  }

  @Override
  public List<ItemProxy> getItemsNeeded() {
    return Collections.emptyList();
  }
}
