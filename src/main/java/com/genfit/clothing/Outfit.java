package com.genfit.clothing;

import java.util.ArrayList;
import java.util.List;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.WeatherEnum;


public class Outfit extends ClosetComponent {
	
	// saves the list of items that the outfit has
	private List<Item> items;
	
	public Outfit(WeatherEnum weather, FormalityEnum formality, PatternEnum pattern, 
			Color color, TypeEnum type, List<Item> items) {
		// TODO: How to set attributes of outfit (some sort of average?)
		super(weather, formality, pattern, color, type);
		this.items = items;
	}
	
	/**
	 * Gets the items that the outfit is composed of.
	 * @return list of items in the outfit
	 */
	public List<Item> getItems() {
		return this.items;
	}


}
