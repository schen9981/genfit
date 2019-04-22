package com.genfit.suggester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.WeatherAttribute;
import com.genfit.clothing.Outfit;

/**
 * Utility class that suggests new outfits based on suggested attributes.
 */
public class OutfitSuggester {
	
  /**
   * Private constructor for utility class.
   */
  private OutfitSuggester() {
  }

  /**
   * Method that gets the outfits to suggest based on the list of attributes
   * generated from the attribute suggestor.
   * @param map of class (corresponding to an attribute) 
   * 		to a list of values for the attributes
   * @return list of outfits to suggest
   */
  private List<Outfit> suggestOutfits(Map<Class, List<Attribute>> attr) {
	  List<Outfit> suggestions = new ArrayList<>();
	  
	  // attribute values for weather
	  List<Attribute> weather = attr.get(WeatherAttribute.class);
	  // attribute values for formality
	  List<Attribute> formality = attr.get(FormalityAttribute.class);
	  // attribute values for pattern
	  List<Attribute> pattern = attr.get(PatternAttribute.class);
	  // attribute values for color
	  List<Attribute> color = attr.get(ColorAttribute.class);
	  
	  // find which list of attributes is the smallest, query on that one
	  
	  // query database for all items that have that attribute
	  
	  // sort through for the other attributes
	  
	  // output suggestions
	  
	  return suggestions;
  }
}
