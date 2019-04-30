package com.genfit.clothing;

import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;

public class Item extends ClosetComponent {

  private int id;
  private String name;
  private SeasonAttribute season;
  private FormalityAttribute formality;
  private PatternAttribute pattern;
  private ColorAttribute color;
  private TypeAttribute type;
  private String image;

  public Item(int id, String name, SeasonAttribute season,
      FormalityAttribute formality, PatternAttribute pattern,
      ColorAttribute color, TypeAttribute type, String image) {
    super(season, formality, pattern, color, type);
    this.id = id;
    this.name = name;
    this.season = season;
    this.formality = formality;
    this.pattern = pattern;
    this.color = color;
    this.type = type;
    this.image = image;
  }

  public int getId() {
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

  public String getImage() {
    return image;
  }
}
