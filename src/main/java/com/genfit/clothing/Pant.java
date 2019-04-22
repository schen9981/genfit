package com.genfit.clothing;

import com.genfit.attribute.WeatherEnum;

public class Pant extends Item {
	
	public Pant(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, ColorEnum color, TypeEnum type) {
		super(weather, formality, pattern, color, type);
	}

}
