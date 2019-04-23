package com.genfit.clothing;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.attribute.attributevals.SeasonEnum;

public class Item extends ClosetComponent {

	private String id;
	private String name;
	private SeasonAttribute season;
	private FormalityAttribute formality;
	private PatternAttribute pattern;
	private ColorAttribute color;
	private TypeAttribute type;

	public Item(String id, String name, SeasonAttribute season,
							FormalityAttribute formality,
							PatternAttribute pattern, ColorAttribute color,
							TypeAttribute type) {
		super(season, formality, pattern, color, type);
		this.id = id;
		this.name = name;
		this.season = season;
		this.formality = formality;
		this.pattern = pattern;
		this.color = color;
		this.type = type;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SeasonAttribute getSeason() {
		return season;
	}

	public FormalityAttribute getFormality() {
		return formality;
	}

	public PatternAttribute getPattern() {
		return pattern;
	}

	public ColorAttribute getColor() {
		return color;
	}

	public TypeAttribute getType() {
		return type;
	}
}
