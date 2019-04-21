package com.genfit.comparators;

import com.genfit.attribute.WeatherAttribute;

import javax.management.Attribute;

public class WeatherAttrComparator implements AttributeComparator<WeatherAttribute> {
  @Override
  public int compare(WeatherAttribute o1, WeatherAttribute o2) {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return false;
  }
}
