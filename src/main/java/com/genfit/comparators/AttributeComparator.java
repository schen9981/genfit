package com.genfit.comparators;

import com.genfit.attribute.Attribute;

import java.util.Comparator;

/**
 * Generic Comparator for attributes.
 *
 * @param <T> Type that extends attribute
 */
interface AttributeComparator<T extends Attribute> extends Comparator<T> {
}
