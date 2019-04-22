package com.genfit.clothing;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user's wardrobe.
 */
public class Wardrobe {
	
	Set<Item> items;
	Set<Outfit> outfits;
	
	/**
	 * Constructor for an initially empty wardrobe.
	 */
	public Wardrobe() {
		this.items = new HashSet<Item>();
		this.outfits = new HashSet<Outfit>();
	}
	
	/**
	 * Adds an outfit to the wardrobe (and items if they don't exist).
	 * @param o Outfit to add
	 */
	public void addOutfit(Outfit o) {
		outfits.add(o);
		items.addAll(o.getItems());
	}
	
	/**
	 * Add an item to the wardrobe.
	 * @param i Item to add
	 */
	public void addItem(Item i) {
		items.add(i);
	}
		
	/**
	 * Gets all the outfits of the wardrobe.
	 * @return outfits of the wardrobe
	 */
	public Set<Outfit> getOutfits() {
		return this.outfits;
	}
	
	/**
	 * Gets all the items of the wardrobe.
	 * @return items of the wardrobe
	 */
	public Set<Item> getItems() {
		return this.items;
	}

}
