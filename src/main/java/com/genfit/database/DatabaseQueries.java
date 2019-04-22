package com.genfit.database;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.WeatherEnum;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueries {
  Connection conn;

  private final String getUserInfoSQL = "SELECT * FROM user WHERE email=?;";
  private final String getItemInfoSQL = "SELECT * FROM item WHERE id=?;";
  private final String getOutfitInfoSQL = "SELECT * FROM outfit WHERE id=?;";
//  private final String getItemsByUserIDSQL = "SELECT * FROM user_item,item WHERE user_item.user_id=? AND user_item.item_id=item.id;";
  private final String getItemsByUserIDSQL = "SELECT * FROM user_item WHERE user_id=?;";
  private final String getOutfitsByUserIDSQL = "SELECT * FROM user_outfit WHERE user_id=?;";

  private PreparedStatement getUserInfoPrep, getItemInfoPrep, getOutfitInfoPrep;
  private PreparedStatement getItemsByUserIDPrep, getOutfitsByUserIDPrep;

  private PreparedStatement getAllItemsByAttributesPrep;


  public DatabaseQueries(Connection conn) {
    try {
      this.conn = conn;
      getUserInfoPrep = conn.prepareStatement(getUserInfoSQL);
      getItemInfoPrep = conn.prepareStatement(getItemInfoSQL);
      getOutfitInfoPrep = conn.prepareStatement(getOutfitInfoSQL);
      getItemsByUserIDPrep = conn.prepareStatement(getItemsByUserIDSQL);
      getOutfitsByUserIDPrep = conn.prepareStatement(getOutfitsByUserIDSQL);
    } catch (SQLException e) {

    }
  }

  public User getUserInfo(String email) throws SQLException {
    getUserInfoPrep.setString(1, email);
    ResultSet rs = getUserInfoPrep.executeQuery();
    rs.next();
    String id = rs.getString(1);
    String name = rs.getString(2);
    rs.close();
    return new User(id, name, email);
  }

  public Item getItemInfo(String id) throws SQLException {
    getItemInfoPrep.setString(1, id);
    ResultSet rs = getItemInfoPrep.executeQuery();
    rs.next();
    String name = rs.getString(2);
    int type = rs.getInt(3);
    int formality = rs.getInt(4);
    int color = Integer.parseInt(rs.getString(5), 16);
    int pattern = rs.getInt(6);
    int weather = rs.getInt(7);
    rs.close();
    return new Item(weather, formality, pattern, new Color(color), type);
  }

  public List<ItemProxy> getAllItemsByAttributes(AttributeEnum attributeNum, List<Attribute> attribute) throws SQLException{
    String[] attributeNames = {"type", "formality", "color", "pattern", "weather"};
    String attributeName = attributeNames[attributeNum];

    String getAllItemsByAttributesSQL = "SELECT * FROM item WHERE ?=?";

    for (int i = 0; i < attribute.size(); i++) {
      getAllItemsByAttributesSQL += " OR ?=?";
    }
    getAllItemsByAttributesSQL += ";";
    getAllItemsByAttributesPrep = conn.prepareStatement(getAllItemsByAttributesSQL);
    for (int i = 1; i <= attribute.size() * 2; i+=2) {
      getAllItemsByAttributesPrep.setString(i, attributeName);
      getAllItemsByAttributesPrep.setString(i + 1, attribute.get(i / 2));
    }

  }

  public Outfit getOutfitInfo(String id) throws SQLException {
    getOutfitInfoPrep.setString(1, id);
    ResultSet rs = getOutfitInfoPrep.executeQuery();
    rs.next();
    String name = rs.getString(2);
    String outer = rs.getString(3);
    String top = rs.getString(4);
    String bottom = rs.getString(5);
    String feet = rs.getString(6);
    rs.close();
    return new Outfit(id, name, outer, top, bottom, feet);
  }

  public List<ItemProxy> getItemsByUserID(String id) throws SQLException {
    List<ItemProxy> itemProxyList = new ArrayList<>();
    getItemsByUserIDPrep.setString(1, id);
    ResultSet rs = getItemsByUserIDPrep.executeQuery();
    while(rs.next()) {
      String itemID = rs.getString(2);
      itemProxyList.add(new ItemProxy(itemID));
    }
    rs.close();
    return itemProxyList;
  }

  public List<OutfitProxy> getOutfitsByUserID(String id) throws SQLException {
    List<OutfitProxy> outfitProxyList = new ArrayList<>();
    getOutfitsByUserIDPrep.setString(1, id);
    ResultSet rs = getOutfitsByUserIDPrep.executeQuery();
    while(rs.next()) {
      String outfitID = rs.getString(2);
      outfitProxyList.add(new OutfitProxy(outfitID));
    }
    rs.close();
    return outfitProxyList;
  }
}
