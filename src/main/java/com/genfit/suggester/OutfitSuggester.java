package com.genfit.suggester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.genfit.attribute.Attribute;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.database.Database;

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
  public List<Outfit> suggestOutfits(Map<Class, List<Attribute>> attr) {
	  
	  // get the class that has the minimum number of attributes to query
	  Class classToQuery = minAttrToQuery(attr);
	  
	  // create set of all other classes to query on (exclude min)
	  Set<Class> otherClasses = attr.keySet();
	  otherClasses.remove(classToQuery);
	  
	  // get list of items that have matching attributes of smallest query
//	  List<Item> match = Database.getItemsOfAttribute(attr.get(classToQuery));
	  
	  // sort through for the other attributes
	  // TODO: write method for sorting through other attributes.
	  
	  List<Outfit> suggestions = new ArrayList<>();
	  return suggestions;
  }
  
  /**
   * Method that determines which class contains the minimum number of attributes.
   * @param attr - map of attributes
   * @return minimum class
   */
  public static Class minAttrToQuery(Map<Class, List<Attribute>> attr) {
	  Class minClass = null;
	  int minSize = Integer.MAX_VALUE;
	  for (Class c : attr.keySet()) {
		  int size = attr.get(c).size();
		  if (size < minSize) {
			  minSize = size;
			  minClass = c;
		  }
	  }
	  return minClass;
  }
  
  
}
