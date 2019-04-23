package com.genfit.users;

import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;

import java.util.List;

public class User {

  private String id;
  private String name;
  private String email;
  private List<ItemProxy> items;
  private List<OutfitProxy> outfits;

  public User(String id, String name, String email,
              List<ItemProxy> items, List<OutfitProxy> outfits) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.items = items;
    this.outfits = outfits;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public List<ItemProxy> getItems() {
    return items;
  }

  public List<OutfitProxy> getOutfits() {
    return outfits;
  }

}
