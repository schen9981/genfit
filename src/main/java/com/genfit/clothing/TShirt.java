package com.genfit.clothing;

import com.genfit.attribute.WeatherEnum;

public class TShirt extends Item {
	
	public TShirt(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, ColorEnum color, TypeEnum type) {
		super(weather, formality, pattern, color, type);
	}

}
