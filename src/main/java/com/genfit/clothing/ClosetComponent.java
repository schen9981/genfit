package com.genfit.clothing;

import java.util.HashMap;
import java.util.Map;

import com.genfit.attribute.Attribute;
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

/**
 * Abstract class that wraps all the attribute/properties 
 * of closet components (ie. item, outfit).
 */
public abstract class ClosetComponent {
	
	// properties of every attribute
	private WeatherAttribute weather;
	private FormalityAttribute formality;
	private PatternAttribute pattern;
	private ColorAttribute color;
	private TypeAttribute type;
	
	/**
	 * Constructor for a closet component.
	 * @param weather - enum for weather property
	 * @param formality - enum for formality property
	 * @param pattern - enum for pattern property
	 * @param color - enum for color property
	 * @param type - enum for type property
	 */
	public ClosetComponent(WeatherAttribute weather, FormalityAttribute formality,
												 PatternAttribute pattern, ColorAttribute color,
												 TypeAttribute type) {
		this.weather = weather;
		this.formality = formality;
		this.pattern = pattern;
		this.color = color;
		this.type = type;
	}
	
	/**
	 * Get the weather attribute for the closet component
	 * @return weather attribute
	 */
	public WeatherAttribute getWeatherAttribute() {
		return this.weather;
	}
	
	/**
	 * Get the formality attribute for the closet component
	 * @return formality attribute
	 */
	public FormalityAttribute getFormalityAttribute() {
		return this.formality;
	}
	
	/**
	 * Get the pattern attribute for the closet component
	 * @return pattern attribute
	 */
	public PatternAttribute getPatternAttribute() {
		return this.pattern;
	}
	
	/**
	 * Get the color attribute for the closet component
	 * @return color attribute
	 */
	public ColorAttribute getColorAttribute() {
		return this.color;
	}
	
	/**
	 * Get the type attribute for the closet component
	 * @return type attribute
	 */
	public TypeAttribute getTypeAttribute() {
		return this.type;
	}
	
	

}
