package com.genfit.clothing;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.WeatherAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.WeatherEnum;

public class Item extends ClosetComponent {

	private String id;
	private String name;
	private WeatherAttribute weather;
	private FormalityAttribute formality;
	private PatternAttribute pattern;
	private ColorAttribute color;
	private TypeEnum type;

	public Item(String id, String name, WeatherAttribute weather,
							FormalityAttribute formality,
							PatternAttribute pattern, ColorAttribute color,
							TypeAttribute type) {
		super(weather, formality, pattern, color, type);
		this.id = id;
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
