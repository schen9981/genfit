package com.genfit.clothing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.WeatherEnum;
<<<<<<< HEAD
import com.genfit.proxy.ItemProxy;
=======
import com.google.common.collect.ImmutableMap;
>>>>>>> ba2cb0fa0c420d4a62457bd8496c53def91a4329


public class Outfit extends ClosetComponent {
	
	// saves the list of items that the outfit has
<<<<<<< HEAD
	private List<ItemProxy> items;
	private String id;
=======
	private Map<TypeEnum, Item> items;
>>>>>>> ba2cb0fa0c420d4a62457bd8496c53def91a4329
	
	public Outfit(WeatherEnum weather, FormalityEnum formality, PatternEnum pattern, 
			Color color, TypeEnum type, List<ItemProxy> items, String id) {
		// TODO: How to set attributes of outfit (some sort of average?)
		super(weather, formality, pattern, color, type);
<<<<<<< HEAD
		this.items = items;
		this.id = id;
=======
		this.items = new HashMap<TypeEnum, Item>();
>>>>>>> ba2cb0fa0c420d4a62457bd8496c53def91a4329
	}
	
	/**
	 * Gets a map (immutable) of the items that the outfit is composed of.
	 * @return map of items in the outfit
	 */
<<<<<<< HEAD
	public List<ItemProxy> getItems() {
		return this.items;
=======
	public ImmutableMap<TypeEnum, Item> getItems() {
		ImmutableMap<TypeEnum, Item> immutable = ImmutableMap.copyOf(this.items);
		return immutable;
>>>>>>> ba2cb0fa0c420d4a62457bd8496c53def91a4329
	}


}
