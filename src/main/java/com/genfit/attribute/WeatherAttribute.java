package com.genfit.attribute;

import com.genfit.attribute.attributevals.WeatherEnum;

public class WeatherAttribute implements Attribute<WeatherEnum> {
  private WeatherEnum weatherEnum;

  WeatherAttribute(WeatherEnum weatherEnum) {
    this.weatherEnum = weatherEnum;
  }

  @Override
  public WeatherEnum getAttributeVal() {
    return weatherEnum;
  }
}
