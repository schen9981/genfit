package com.genfit.clothing;

import com.genfit.attribute.WeatherEnum;

public class Skirt extends Item {
	
	public Skirt(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, ColorEnum color, TypeEnum type) {
		super(weather, formality, pattern, color, type);
	}

}
