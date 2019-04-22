package com.genfit.suggester;

import com.genfit.clothing.Outfit;

/**
 * Utility class that suggests new outfits based on suggested attributes.
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
