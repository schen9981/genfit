package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.WeatherEnum;

public class Sandals extends Item {
	
	public Sandals(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, Color color, TypeEnum type, String id, String name) {
		super(weather, formality, pattern, color, type, id, name);
	}

}
