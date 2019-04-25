package com.genfit.database;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Database {
  private Connection conn;

  //Check statements
  private final String checkLoginSQL = "SELECT * FROM user WHERE email = ? "
      + "AND password = ?;";
  private final String checkSignupSQL = "SELECT * FROM user WHERE email = ?;";
  private PreparedStatement checkLoginPrep, checkSignupPrep;

  //Get Statements
  private final String getUserInfoSQL = "SELECT * FROM user WHERE email=?;";
  private final String getItemInfoSQL = "SELECT * FROM item WHERE id=?;";
  private final String getOutfitInfoSQL = "SELECT * FROM outfit WHERE id=?;";
  private final String getItemsByUserIDSQL = "SELECT * FROM user_item WHERE "
          + "user_id=?;";
  private final String getOutfitsByUserIDSQL = "SELECT * FROM user_outfit "
          + "WHERE user_id=?;";
  private PreparedStatement getUserInfoPrep, getItemInfoPrep, getOutfitInfoPrep;
  private PreparedStatement getItemsByUserIDPrep, getOutfitsByUserIDPrep;
  private PreparedStatement getAllItemsByAttributesPrep;

  //Add Statements
  private final String addUserSQL = "INSERT INTO user (name, email, password)"
          + " values (?, ?, ?);";
  private PreparedStatement addUserPrep;

  private final String addItemSQL = "INSERT INTO item"
          + " (name, type, formality, color, pattern, weather)"
          + " VALUES (?, ?, ?, ?, ?, ?);";
  private final String addItemToUserSQL = "INSERT INTO item"
          + "(user_id, item_id) VALUES (?, ?);";
  private PreparedStatement addItemPrep, addItemToUserPrep;

  private final String addOutfitSQL = "INSERT INTO outfit"
          + " (name, outer, top, bottom, feet) VALUES (?, ?, ?, ?, ?);";
  private final String addOutfitToUserSQL = "INSERT INTO user_outfit"
          + " (user_id, outfit_id) VALUES (?, ?);";
  private PreparedStatement addOutfitPrep, addOutfitToUserPrep;

  //Delete Statements
  private final String deleteUserSQL = "DELETE FROM user WHERE id=?;";
  private final String deleteAllUserItemsSQL = "DELETE FROM user_item WHERE "
          + "user_id=?;";
  private final String deleteAllUserOutfitsSQL = "DELETE FROM user_outfit "
          + "WHERE user_id=?;";
  private PreparedStatement deleteUserPrep, deleteAllUserItemsPrep,
          deleteAllUserOutfitsPrep;

  private final String deleteItemSQL = "DELETE FROM item WHERE id=?;";
  private final String deleteUserItemSQL = "DELETE FROM user_item WHERE "
          + "user_id=? AND item_id=?;";
  private PreparedStatement deleteItemPrep, deleteUserItemPrep;

  private final String deleteOutfitSQL = "DELETE FROM outfit WHERE id=?;";
  private final String deleteUserOutfitSQL = "DELETE FROM user_item WHERE "
          + "user_id=? AND outfit_id=?;";
  private PreparedStatement deleteOutfitPrep, deleteUserOutfitPrep;

  //Misc Statements
  private final String lastInsertIDSQL = "SELECT LAST_INSERT_ID();";
  private PreparedStatement lastInsertID;

  private LoadingCache<String, User> userCache;
  private LoadingCache<Integer, Item> itemCache;
  private LoadingCache<Integer, Outfit> outfitCache;

  public Database(Connection conn) {
    try {
      this.conn = conn;
      Statement stmt= conn.createStatement();
      stmt.execute("USE genfit;");

      this.checkLoginPrep = conn.prepareStatement(this.checkLoginSQL);
      this.checkSignupPrep = conn.prepareStatement(this.checkSignupSQL);
      this.getUserInfoPrep = conn.prepareStatement(this.getUserInfoSQL);
      this.getItemInfoPrep = conn.prepareStatement(this.getItemInfoSQL);
      this.getOutfitInfoPrep = conn.prepareStatement(this.getOutfitInfoSQL);
      this.getItemsByUserIDPrep = conn
              .prepareStatement(this.getItemsByUserIDSQL);
      this.getOutfitsByUserIDPrep = conn
              .prepareStatement(this.getOutfitsByUserIDSQL);

      this.addUserPrep = conn.prepareStatement(this.addUserSQL);
      this.addItemPrep = conn.prepareStatement(this.addItemSQL);
      this.addItemToUserPrep = conn.prepareStatement(this.addItemToUserSQL);
      this.addOutfitPrep = conn.prepareStatement(this.addOutfitSQL);
      this.addOutfitToUserPrep = conn.prepareStatement(this.addOutfitToUserSQL);

      this.deleteAllUserItemsPrep =
              conn.prepareStatement(this.deleteAllUserItemsSQL);
      this.deleteAllUserOutfitsPrep =
              conn.prepareStatement(this.deleteAllUserOutfitsSQL);
      this.deleteUserPrep = conn.prepareStatement(this.deleteUserSQL);
      this.deleteItemPrep = conn.prepareStatement(this.deleteItemSQL);
      this.deleteUserItemPrep = conn.prepareStatement(this.deleteUserItemSQL);
      this.deleteOutfitPrep = conn.prepareStatement(this.deleteOutfitSQL);
      this.deleteUserOutfitPrep = conn.prepareStatement(this.deleteUserOutfitSQL);
      this.lastInsertID = conn.prepareStatement(this.lastInsertIDSQL);
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
            .build(new CacheLoader<Integer, Item>() {
              @Override
              public Item load(Integer key) throws Exception {
                return Database.this.getItemInfo(key);
              }
            });

    this.outfitCache = CacheBuilder.newBuilder().maximumSize(maxSize)
            .expireAfterWrite(expire, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, Outfit>() {
              @Override
              public Outfit load(Integer key) throws Exception {
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
  public Item getItemBean(int id) throws Exception {
    return this.itemCache.get(id);
  }

  /**
   * Called by OutfitProxy instances.
   *
   * @param id oof outfit
   * @return Outfit instance
   * @throws Exception
   */
  public Outfit getOutfitBean(int id) throws Exception {
    return this.outfitCache.get(id);
  }


  public boolean checkLogin(String username, String hashPwd) throws Exception{
    this.checkLoginPrep.setString(1, username);
    this.checkLoginPrep.setString(2, hashPwd);
    ResultSet rs = this.checkLoginPrep.executeQuery();
    boolean success = false;
    if (rs.next()) {
      success = true;
    }
    rs.close();
    return success;
  }

  public boolean checkSignup(String username) throws Exception{

    this.checkSignupPrep.setString(1, username);
    ResultSet rs = this.checkSignupPrep.executeQuery();
    boolean success = true;
    if (rs.next()) {
      success = false;
    }
    rs.close();
    return success;
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
    int id = rs.getInt(1);
    String name = rs.getString(2);
    rs.close();
    return new User(id, name, email, this.getItemsByUserID(id),
            this.getOutfitsByUserID(id));
  }

  /**
   * Called by Item LoadingCache if needed.
   *
   * @param id of item
   * @return Item instance
   * @throws SQLException
   */
  private Item getItemInfo(int id) throws SQLException {
    this.getItemInfoPrep.setInt(1, id);
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
    // return new Item(id, name, season, formality, pattern, color, type);
    return null;
  }

  // TODO: @lawrence will modify this
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
      int itemID = rs.getInt(1);
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
  private Outfit getOutfitInfo(int id) throws SQLException {
    this.getOutfitInfoPrep.setInt(1, id);
    ResultSet rs = this.getOutfitInfoPrep.executeQuery();
    rs.next();

    String name = rs.getString(2);
    int outerID = rs.getInt(3);
    int topID = rs.getInt(4);
    int bottomID = rs.getInt(5);
    int feetID = rs.getInt(6);
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
  private List<ItemProxy> getItemsByUserID(int id) throws SQLException {
    List<ItemProxy> itemProxyList = new ArrayList<>();
    this.getItemsByUserIDPrep.setInt(1, id);
    ResultSet rs = this.getItemsByUserIDPrep.executeQuery();
    while (rs.next()) {
      int itemID = rs.getInt(2);
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
  private List<OutfitProxy> getOutfitsByUserID(int id) throws SQLException {
    List<OutfitProxy> outfitProxyList = new ArrayList<>();
    this.getOutfitsByUserIDPrep.setInt(1, id);
    ResultSet rs = this.getOutfitsByUserIDPrep.executeQuery();
    while (rs.next()) {
      int outfitID = rs.getInt(2);
      outfitProxyList.add(new OutfitProxy(this, outfitID));
    }
    rs.close();
    return outfitProxyList;
  }


  public void addUser(String name, String email, String hashPwd)
      throws SQLException {
    this.addUserPrep.setString(1, name);
    this.addUserPrep.setString(2, email);
    this.addUserPrep.setString(3, hashPwd);
    this.addUserPrep.executeUpdate();
  }

  public void addItem(User user, String itemName) throws SQLException {
    //TODO item parameters
    this.addItemPrep.setString(1, itemName);
    this.addItemPrep.setInt(2, 0);
    this.addItemPrep.setInt(3, 0);
    this.addItemPrep.setString(4, "0xFFFFFF");
    this.addItemPrep.setInt(5, 0);
    this.addItemPrep.setInt(6, 0);
    this.addItemPrep.executeUpdate();

    ResultSet rs = this.lastInsertID.executeQuery();
    rs.next();
    int itemID = rs.getInt(1);

    this.addItemToUserPrep.setInt(1, user.getId());
    this.addItemToUserPrep.setInt(2, itemID);
    this.addItemToUserPrep.executeUpdate();
  }

  public void addOutfit(User user, String outfitName, Map<TypeEnum,
          ItemProxy> items) throws SQLException {
    ItemProxy outerItem = items.get(TypeEnum.OUTER);
    ItemProxy topItem = items.get(TypeEnum.TOP);
    ItemProxy bottomItem = items.get(TypeEnum.BOTTOM);
    ItemProxy shoeItem = items.get(TypeEnum.SHOES);

    this.addOutfitPrep.setString(1, outfitName);
    this.addOutfitPrep.setInt(2, outerItem.getId());
    this.addOutfitPrep.setInt(3, topItem.getId());
    this.addOutfitPrep.setInt(4, bottomItem.getId());
    this.addOutfitPrep.setInt(5, shoeItem.getId());
    this.addOutfitPrep.executeUpdate();

    ResultSet rs = this.lastInsertID.executeQuery();
    rs.next();
    int outfitID = rs.getInt(1);

    this.addOutfitToUserPrep.setInt(1, user.getId());
    this.addOutfitToUserPrep.setInt(2, outfitID);
    this.addOutfitToUserPrep.executeUpdate();
  }

  /**
   * Deletes a user from the user Table, all user item and outfit references
   * from the user_item and user_outfit tables respectively.
   *
   * @param user
   * @throws SQLException
   */
  private void deleteUser(User user) throws SQLException {
    this.deleteUserPrep.setInt(1, user.getId());
    this.deleteUserPrep.executeUpdate();
    this.deleteAllUserItemsPrep.setInt(1, user.getId());
    this.deleteAllUserItemsPrep.executeUpdate();
    this.deleteAllUserOutfitsPrep.setInt(1, user.getId());
    this.deleteAllUserOutfitsPrep.executeUpdate();
  }

  /**
   * Deletes an item from the item table and its reference from the
   * user_item table.
   *
   * @param user - The User that owns the item.
   * @param item - The item to be deleted.
   * @throws SQLException
   */
  private void deleteItem(User user, Item item) throws SQLException {
    //TODO: delete item (might be referenced by other users)?
//    deleteItemPrep.setString(1, item.getId());
//    deleteItemPrep.executeUpdate();
    this.deleteUserItemPrep.setInt(1, user.getId());
    this.deleteUserItemPrep.setInt(2, item.getId());
    this.deleteUserItemPrep.executeUpdate();
  }

  /**
   * Deletes an outfit from the outfit table and its reference in the
   * user_outfit table.
   *
   * @param user   - The user that owns the outfit.
   * @param outfit - The Outfit to be deleted.
   * @throws SQLException
   */
  private void deleteOutfit(User user, Outfit outfit) throws SQLException {
    //TODO: delete outfit (might be referenced by other users)?
//    deleteOutfitPrep.setString(1, outfit.getId());
//    deleteOutfitPrep.executeUpdate();
    this.deleteUserOutfitPrep.setInt(1, user.getId());
    this.deleteUserOutfitPrep.setInt(2, outfit.getId());
    this.deleteUserOutfitPrep.executeUpdate();
  }

}
