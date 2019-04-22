package com.genfit.clothing;

import java.util.HashMap;
import java.util.Map;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.WeatherAttribute;

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
	public ClosetComponent(WeatherEnum weather, FormalityEnum formality, 
			PatternEnum pattern, ColorEnum color, TypeEnum type) {
		this.weather = new WeatherAttribute(weather);
		this.formality = new FormalityAttribute(formality);
		this.pattern = new PatternAttribute(pattern);
		this.color = new ColorAttribute(color);
		this.type = new TypeAttribute(type);
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
