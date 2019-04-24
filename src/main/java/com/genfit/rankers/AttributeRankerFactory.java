package com.genfit.rankers;

import com.genfit.attribute.Attribute;
import com.genfit.proxy.ItemProxy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that ranks by attributes.
 */
public class AttributeRankerFactory {
  private List<ItemProxy> itemsAdded;

  public AttributeRankerFactory() {
    this.itemsAdded = new LinkedList<>();
  }

  /**
   * Adds an item whose attributes will be considered by the ranker.
   *
   * @param item item to add
   * @return whether add operation completed successfully
   */
  public boolean addItem(ItemProxy item) {
    if (item == null) {
      return false;
    } else {
      try {
        this.itemsAdded.add(item);
      } catch (Exception e) {
        return false;
      }
      return true;
    }
  }

  /**
   * Clears all items to be considered.
   *
   * @return whether clear operation completed successfully
   */
  public boolean clear() {
    try {
      this.itemsAdded.clear();
      return true;
    } catch (UnsupportedOperationException e) {
      return false;
    }
  }

  /**
   * Returns list of ranked attributes based on the items that were input;
   */
  public <T extends Attribute> List<T> rankAttributes(AttributeRanker<T> attrRanker) {
    return attrRanker.rankByAttribute(this.itemsAdded);
  }
}
