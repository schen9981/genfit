package com.genfit.attribute;

import com.genfit.attribute.attributevals.SubtypeEnum;

/**
 * Created by ericwang on 2019/5/2.
 */
public class SubtypeAttribute implements Attribute<SubtypeEnum> {
  private SubtypeEnum subtypeEnum;

  public SubtypeAttribute(SubtypeEnum subtypeEnum) {
    this.subtypeEnum = subtypeEnum;
  }

  @Override
  public SubtypeEnum getAttributeVal() {
    return this.subtypeEnum;
  }

  @Override
  public String getAttributeName() {
    return "subtype";
  }

  @Override
  public String toString() {
    return "SubtypeAttribute{" +
            "subtypeEnum=" + subtypeEnum +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SubtypeAttribute that = (SubtypeAttribute) o;

    return subtypeEnum == that.subtypeEnum;
  }

  @Override
  public int hashCode() {
    return subtypeEnum != null ? subtypeEnum.hashCode() : 0;
  }
}
