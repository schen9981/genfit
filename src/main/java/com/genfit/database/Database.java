package com.genfit.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.AttributeEnum;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.users.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class Database {
  private Connection conn;
  private final String getUserInfoSQL = "SELECT * FROM user WHERE email=?;";
  private final String getItemInfoSQL = "SELECT * FROM item WHERE id=?;";
  private final String getOutfitInfoSQL = "SELECT * FROM outfit WHERE id=?;";
  // private final String getItemsByUserIDSQL = "SELECT * FROM user_item,
  // item WHERE user_item.user_id=? AND user_item.item_id=item.id;";
  private final String getItemsByUserIDSQL = "SELECT * FROM user_item WHERE "
      + "user_id=?;";
  private final String getOutfitsByUserIDSQL = "SELECT * FROM user_outfit "
      + "WHERE user_id=?;";
  private PreparedStatement getUserInfoPrep, getItemInfoPrep, getOutfitInfoPrep;
  private PreparedStatement getItemsByUserIDPrep, getOutfitsByUserIDPrep;
  private PreparedStatement getAllItemsByAttributesPrep;

  private LoadingCache<String, User> userCache;
  private LoadingCache<String, Item> itemCache;
  private LoadingCache<String, Outfit> outfitCache;

  public Database(Connection conn) {
    try {
      this.conn = conn;
      this.getUserInfoPrep = conn.prepareStatement(this.getUserInfoSQL);
      this.getItemInfoPrep = conn.prepareStatement(this.getItemInfoSQL);
      this.getOutfitInfoPrep = conn.prepareStatement(this.getOutfitInfoSQL);
      this.getItemsByUserIDPrep = conn
          .prepareStatement(this.getItemsByUserIDSQL);
      this.getOutfitsByUserIDPrep = conn
          .prepareStatement(this.getOutfitsByUserIDSQL);
    } catch (SQLException e) {
      System.out.println("ERROR: SQLExeception when prepare statement"
          + "in Database constructor");
    }
    this.instantiateCacheLoader();
  }

  /**
   * Setup the loading cache for user, item, and outfit.
   */
  private void instantiateCacheLoader() {
    final int maxSize = 10000;
    final int expire = 10;

    this.userCache = CacheBuilder.newBuilder().maximumSize(maxSize)
        .expireAfterWrite(expire, TimeUnit.MINUTES)
        .build(new CacheLoader<String, User>() {
          @Override
          public User load(String key) throws Exception {
            return Database.this.getUserInfo(key);
          }
        });

    this.itemCache = CacheBuilder.newBuilder().maximumSize(maxSize)
        .expireAfterWrite(expire, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Item>() {
          @Override
          public Item load(String key) throws Exception {
            return Database.this.getItemInfo(key);
          }
        });

    this.outfitCache = CacheBuilder.newBuilder().maximumSize(maxSize)
        .expireAfterWrite(expire, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Outfit>() {
          @Override
          public Outfit load(String key) throws Exception {
            return Database.this.getOutfitInfo(key);
          }
        });
  }

  /**
   * Called by UserProxy instances.
   *
   * @param email of user
   * @return User instance
   * @throws Exception
   */
  public User getUserBean(String email) throws Exception {
    return this.userCache.get(email);
  }

  /**
   * Called by ItemProxy instances.
   *
   * @param id of item
   * @return Item instance
   * @throws Exception
   */
  public Item getItemBean(String id) throws Exception {
    return this.itemCache.get(id);
  }

  /**
   * Called by OutfitProxy instances.
   *
   * @param id oof outfit
   * @return Outfit instance
   * @throws Exception
   */
  public Outfit getOutfitBean(String id) throws Exception {
    return this.outfitCache.get(id);
  }

  /**
   * Called by User LoadingCache if needed.
   *
   * @param email of user
   * @return User instance
   * @throws SQLException
   */
  private User getUserInfo(String email) throws SQLException {
    this.getUserInfoPrep.setString(1, email);
    ResultSet rs = this.getUserInfoPrep.executeQuery();
    rs.next();
    String id = rs.getString(1);
    String name = rs.getString(2);
    rs.close();
    return new User(id, name, email, this.getItemsByUserID(email),
        this.getOutfitsByUserID(email));
  }

  /**
   * Called by Item LoadingCache if needed.
   *
   * @param id of item
   * @return Item instance
   * @throws SQLException
   */
  private Item getItemInfo(String id) throws SQLException {
    this.getItemInfoPrep.setString(1, id);
    ResultSet rs = this.getItemInfoPrep.executeQuery();
    rs.next();
    String name = rs.getString(2);
    TypeAttribute type = new TypeAttribute(TypeEnum.values()[rs.getInt(3)]);
    FormalityAttribute formality = new FormalityAttribute(
        FormalityEnum.values()[rs.getInt(4)]);
    ColorAttribute color = new ColorAttribute(
        new Color(Integer.parseInt(rs.getString(5), 16)));
    PatternAttribute pattern = new PatternAttribute(
        PatternEnum.values()[rs.getInt(6)]);
    SeasonAttribute season = new SeasonAttribute(
        SeasonEnum.values()[rs.getInt(7)]);
    rs.close();
    // TODO: How to instantiate Item of specific type
//    return new Item(id, name, season, formality, pattern, color, type);
    return null;
  }

  public List<ItemProxy> getAllItemsByAttributes(AttributeEnum attributeEnum,
      List<Attribute> attribute) throws SQLException {
    String attributeName = attributeEnum.toString();

    String getAllItemsByAttributesSQL = "SELECT * FROM item WHERE ?=?";

    for (int i = 1; i < attribute.size(); i++) {
      getAllItemsByAttributesSQL += " OR ?=?";
    }
    getAllItemsByAttributesSQL += ";";
    this.getAllItemsByAttributesPrep = this.conn
        .prepareStatement(getAllItemsByAttributesSQL);

    if (attributeEnum == AttributeEnum.COLOR) {
      for (int i = 1; i <= attribute.size() * 2; i += 2) {
        this.getAllItemsByAttributesPrep.setString(i, attributeName);
        Color color = (Color) attribute.get(i / 2).getAttributeVal();
        this.getAllItemsByAttributesPrep.setString(i + 1, color.toString());
      }
    } else {
      for (int i = 1; i <= attribute.size() * 2; i += 2) {
        this.getAllItemsByAttributesPrep.setString(i, attributeName);
        Enum e = (Enum) attribute.get(i / 2).getAttributeVal();
        this.getAllItemsByAttributesPrep.setInt(i + 1, e.ordinal());
      }
    }

    List<ItemProxy> itemProxyList = new ArrayList<>();
    ResultSet rs = this.getAllItemsByAttributesPrep.executeQuery();
    while (rs.next()) {
      String itemID = rs.getString(1);
      itemProxyList.add(new ItemProxy(this, itemID));
    }
    rs.close();
    return itemProxyList;
  }

  /**
   * Called by Outfit LoadingCache if needed.
   *
   * @param id of outfit
   * @return Outfit instance
   * @throws SQLException
   */
  private Outfit getOutfitInfo(String id) throws SQLException {
    this.getOutfitInfoPrep.setString(1, id);
    ResultSet rs = this.getOutfitInfoPrep.executeQuery();
    rs.next();

    String name = rs.getString(2);
    TypeEnum type = TypeEnum.values()[rs.getInt(3)];
    FormalityEnum formality = FormalityEnum.values()[rs.getInt(4)];
    Color color = new Color(Integer.parseInt(rs.getString(5), 16));
    PatternEnum pattern = PatternEnum.values()[rs.getInt(6)];
    SeasonEnum weather = SeasonEnum.values()[rs.getInt(7)];
    String outerID = rs.getString(8);
    String topID = rs.getString(9);
    String bottomID = rs.getString(10);
    String feetID = rs.getString(11);
    rs.close();

    ItemProxy outer = new ItemProxy(this, outerID);
    ItemProxy top = new ItemProxy(this, topID);
    ItemProxy bottom = new ItemProxy(this, bottomID);
    ItemProxy feet = new ItemProxy(this, feetID);

    Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
    itemMap.put(TypeEnum.OUTER, outer);
    itemMap.put(TypeEnum.TOP, top);
    itemMap.put(TypeEnum.BOTTOM, bottom);
    itemMap.put(TypeEnum.SHOES, feet);

    // TODO: Change parameters for Outfit constructor
//    return new Outfit(id, name, itemMap);
    return null;
  }

  /**
   * Called by getUserInfo() to instantiate a User instance.
   *
   * @param id of user
   * @return List of ItemProxy instances
   * @throws SQLException
   */
  private List<ItemProxy> getItemsByUserID(String id) throws SQLException {
    List<ItemProxy> itemProxyList = new ArrayList<>();
    this.getItemsByUserIDPrep.setString(1, id);
    ResultSet rs = this.getItemsByUserIDPrep.executeQuery();
    while (rs.next()) {
      String itemID = rs.getString(2);
      itemProxyList.add(new ItemProxy(this, itemID));
    }
    rs.close();
    return itemProxyList;
  }

  /**
   * Called by getUserInfo() to instantiate a User instance.
   *
   * @param id of user
   * @return List of OutfitProxy instances
   * @throws SQLException
   */
  private List<OutfitProxy> getOutfitsByUserID(String id) throws SQLException {
    List<OutfitProxy> outfitProxyList = new ArrayList<>();
    this.getOutfitsByUserIDPrep.setString(1, id);
    ResultSet rs = this.getOutfitsByUserIDPrep.executeQuery();
    while (rs.next()) {
      String outfitID = rs.getString(2);
      outfitProxyList.add(new OutfitProxy(this, outfitID));
    }
    rs.close();
    return outfitProxyList;
  }
}
