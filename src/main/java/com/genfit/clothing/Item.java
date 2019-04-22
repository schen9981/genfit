package com.genfit.clothing;

import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.WeatherEnum;

public abstract class Item extends ClosetComponent {
	
	public Item(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, Color color, TypeEnum type) {
		super(weather, formality, pattern, color, type);
	}

}
