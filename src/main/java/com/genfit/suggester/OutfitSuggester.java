package com.genfit.suggester;

import com.genfit.clothing.Outfit;

/**
 * Utility class that suggests new outfits given a partial outfit.
 */
public class OutfitSuggester {
  /**
   * Private constructor for utility class.
   */
  private OutfitSuggester() {
  }

  private Outfit suggestOutfits() {
    return new Outfit(null, null, null, null, null, null);
  }
}
