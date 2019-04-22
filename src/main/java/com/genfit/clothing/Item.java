package com.genfit.clothing;

import com.genfit.attribute.WeatherEnum;

public abstract class Item extends ClosetComponent {
	
	public Item(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, ColorEnum color, TypeEnum type) {
		super(weather, formality, pattern, color, type);
	}

}
