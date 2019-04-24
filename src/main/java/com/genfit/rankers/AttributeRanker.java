package com.genfit.rankers;

import com.genfit.attribute.Attribute;
import com.genfit.proxy.ItemProxy;

import java.util.List;

interface AttributeRanker<T extends Attribute> {
  List<T> rankByAttribute(List<ItemProxy> items);
}
