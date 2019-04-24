package com.genfit.attribute;

/**
 * Interface for an attribute description.
 *
 * @param <T> type of the possible attribute values.
 */
public interface Attribute<T> {
  /**
   * Gets value of the attribute.
   *
   * @return value of the attribute.
   */
  T getAttributeVal();
}
