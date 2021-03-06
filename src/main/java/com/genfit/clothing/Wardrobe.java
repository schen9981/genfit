package com.genfit.clothing;

import java.util.HashSet;
import java.util.Set;

import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

/**
 * Represents a user's wardrobe.
 */
public class Wardrobe {

  private Set<ItemProxy> items;
  private Set<OutfitProxy> outfits;

  /**
   * Constructor for an initially empty wardrobe.
   */
  public Wardrobe() {
    this.items = new HashSet<ItemProxy>();
    this.outfits = new HashSet<OutfitProxy>();
  }

  // TODO: adds the proxy or the item/outfit?

  /**
   * Adds an outfit to the wardrobe (and items if they don't exist).
   *
   * @param o Outfit to add
   */
  public void addOutfit(OutfitProxy o) {
    this.outfits.add(o);
    this.items.addAll(o.getItems().values());
  }

  /**
   * Add an item to the wardrobe.
   *
   * @param i Item to add
   */
  public void addItem(ItemProxy i) {
    this.items.add(i);
  }

  /**
   * Gets all the outfits of the wardrobe.
   *
   * @return outfits of the wardrobe
   */
  public Set<OutfitProxy> getOutfits() {
    return this.outfits;
  }

  /**
   * Gets all the items of the wardrobe.
   *
   * @return items of the wardrobe
   */
  public Set<ItemProxy> getItems() {
    return this.items;
  }

}
