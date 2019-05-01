package com.genfit.database;

import java.lang.reflect.Type;
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

import org.mindrot.jbcrypt.BCrypt;

import com.genfit.attribute.Attribute;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.proxy.UserProxy;
import com.genfit.users.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.mindrot.jbcrypt.BCrypt;

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

  // Check statements
  private final String checkLoginSQL = "SELECT * FROM user WHERE email = ?;";
  private final String checkSignupSQL = "SELECT * FROM user WHERE email = ?;";
  private PreparedStatement checkLoginPrep, checkSignupPrep;

  // Get Statements
  private final String getUserInfoSQL = "SELECT * FROM user WHERE email=?;";
  private final String getItemInfoSQL = "SELECT * FROM item WHERE id=?;";
  private final String getOutfitInfoSQL = "SELECT * FROM outfit WHERE id=?;";
  private final String getItemsByUserIDSQL = "SELECT * FROM user_item WHERE "
          + "user_id=?;";
  private final String getOutfitsByUserIDSQL = "SELECT * FROM user_outfit "
      + "WHERE user_id=?;";
  private final String getOutfitLikesSQL = "SELECT * FROM outfit WHERE id=?"
          + "WHERE user_id=?;";
  private PreparedStatement getUserInfoPrep, getItemInfoPrep, getOutfitInfoPrep;
  private PreparedStatement getItemsByUserIDPrep, getOutfitsByUserIDPrep;
  private PreparedStatement getAllItemsByAttributesPrep;
  private PreparedStatement getOutfitLikesPrep;

  // Add Statements
  private final String addUserSQL = "INSERT INTO user (name, email, password)"
          + " values (?, ?, ?);";
  private PreparedStatement addUserPrep;

  private final String addItemSQL = "INSERT IGNORE INTO item"
      + " (name, type, formality, color, pattern, season, image)"
      + " VALUES (?, ?, ?, ?, ?, ?, ?);";
  private final String addItemToUserSQL = "INSERT INTO user_item "
          + "(user_id, item_id) VALUES (?, ?);";
  private PreparedStatement addItemPrep, addItemToUserPrep;

  private final String addOutfitSQL = "INSERT IGNORE INTO outfit"
          + " (name, `outer`, top, bottom, feet) VALUES (?, ?, ?, ?, ?);";
  private final String addOutfitToUserSQL = "INSERT INTO user_outfit"
          + " (user_id, outfit_id) VALUES (?, ?);";
  private PreparedStatement addOutfitPrep, addOutfitToUserPrep;

  // Delete Statements
  private final String deleteUserSQL = "DELETE FROM user WHERE id=?;";
  private final String deleteAllUserItemsSQL = "DELETE FROM user_item WHERE "
          + "user_id=?;";
  private final String deleteAllUserOutfitsSQL = "DELETE FROM user_outfit "
          + "WHERE user_id=?;";
  private final String incrementLikesSQL = "UPDATE outfit SET likes = "
      + "(likes + 1) WHERE id = ?";
  private PreparedStatement deleteUserPrep, deleteAllUserItemsPrep,
          deleteAllUserOutfitsPrep, incrementLikesPrep;

  private final String deleteItemSQL = "DELETE FROM item WHERE id=?;";
  private final String deleteUserItemSQL = "DELETE FROM user_item WHERE "
          + "user_id=? AND item_id=?;";
  private PreparedStatement deleteItemPrep, deleteUserItemPrep;

  private final String deleteOutfitSQL = "DELETE FROM outfit WHERE id=?;";
  private final String deleteUserOutfitSQL = "DELETE FROM user_item WHERE "
          + "user_id=? AND outfit_id=?;";
  private PreparedStatement deleteOutfitPrep, deleteUserOutfitPrep;

  // Misc Statements
  private final String lastInsertIDSQL = "SELECT LAST_INSERT_ID();";
  private final String changePasswordSQL = "UPDATE user SET password = ? "
          + "WHERE email = ?";
  private PreparedStatement changePasswordPrep;
  private PreparedStatement lastInsertID;
  private LoadingCache<String, User> userCache;
  private LoadingCache<Integer, Item> itemCache;
  private LoadingCache<Integer, Outfit> outfitCache;


  private Map<Integer, String> defaultImageMap = new HashMap<>();
  public Database(Connection conn) {
    try {
      this.conn = conn;
      Statement stmt = conn.createStatement();
      stmt.execute("USE genfit;");
      this.changePasswordPrep = conn.prepareStatement(this.changePasswordSQL);
      this.checkLoginPrep = conn.prepareStatement(this.checkLoginSQL);
      this.checkSignupPrep = conn.prepareStatement(this.checkSignupSQL);
      this.getUserInfoPrep = conn.prepareStatement(this.getUserInfoSQL);
      this.getItemInfoPrep = conn.prepareStatement(this.getItemInfoSQL);
      this.getOutfitInfoPrep = conn.prepareStatement(this.getOutfitInfoSQL);
      this.getItemsByUserIDPrep = conn
              .prepareStatement(this.getItemsByUserIDSQL);
      this.getOutfitsByUserIDPrep = conn
          .prepareStatement(this.getOutfitsByUserIDSQL);
      this.getOutfitLikesPrep = conn.prepareStatement(this.getOutfitLikesSQL);

      this.addUserPrep = conn.prepareStatement(this.addUserSQL);
      this.addItemPrep = conn.prepareStatement(this.addItemSQL);
      this.addItemToUserPrep = conn.prepareStatement(this.addItemToUserSQL);
      this.addOutfitPrep = conn.prepareStatement(this.addOutfitSQL);
      this.addOutfitToUserPrep = conn.prepareStatement(this.addOutfitToUserSQL);
      this.incrementLikesPrep = conn.prepareStatement(this.incrementLikesSQL);

      this.deleteAllUserItemsPrep = conn
              .prepareStatement(this.deleteAllUserItemsSQL);
      this.deleteAllUserOutfitsPrep = conn
              .prepareStatement(this.deleteAllUserOutfitsSQL);
      this.deleteUserPrep = conn.prepareStatement(this.deleteUserSQL);
      this.deleteItemPrep = conn.prepareStatement(this.deleteItemSQL);
      this.deleteUserItemPrep = conn.prepareStatement(this.deleteUserItemSQL);
      this.deleteOutfitPrep = conn.prepareStatement(this.deleteOutfitSQL);
      this.deleteUserOutfitPrep = conn
              .prepareStatement(this.deleteUserOutfitSQL);
      this.lastInsertID = conn.prepareStatement(this.lastInsertIDSQL);

      defaultImageMap.put(TypeEnum.OUTER.ordinal(), "https://s3.amazonaws.com/cs32-term-project-s3-bucket/outer_jacket.png");
      defaultImageMap.put(TypeEnum.TOP.ordinal(), "https://s3.amazonaws.com/cs32-term-project-s3-bucket/tshirt.png");
      defaultImageMap.put(TypeEnum.BOTTOM.ordinal(), "https://s3.amazonaws.com/cs32-term-project-s3-bucket/pants.png");
      defaultImageMap.put(TypeEnum.SHOES.ordinal(), "https://s3.amazonaws.com/cs32-term-project-s3-bucket/sneakers.png");
    } catch (SQLException e) {
      System.out.println("ERROR: SQLExeception when prepare statement"
              + "in Database constructor");
    }
    this.instantiateCacheLoader();
  }

  /**
   * Parses a csv of hex values from the database and returns a list of colors.
   *
   * @param colors - the csv String of colors.
   * @return - a list of colors.
   */
  private List<Color> parseColorCSV(String colors) {
    String[] splitComma = colors.split(",");
    List<Color> colorList = new ArrayList<>();
    for (String colorString : splitComma) {
      colorList.add(new Color(Integer.parseInt(colorString, 16)));
    }
    return colorList;
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

  public void changePassword(String email, String newPwdHash) throws Exception {
    this.changePasswordPrep.setString(1, newPwdHash);
    this.changePasswordPrep.setString(2, email);
    this.changePasswordPrep.executeUpdate();
  }

  public boolean checkLogin(String username, String clientHashPwd) throws Exception {
    this.checkLoginPrep.setString(1, username);
    ResultSet rs = this.checkLoginPrep.executeQuery();
    String storedHash = null;
    if (rs.next()) {
      storedHash = rs.getString(4);
//      System.out.println(storedHash);
    }
    rs.close();

    if (null == storedHash || !storedHash.startsWith("$2a$")) {
//      throw new IllegalArgumentException("Invalid hash provided for
//      comparison");
      return false;
    }

    return BCrypt.checkpw(clientHashPwd, storedHash);
  }

  public boolean checkSignup(String username) throws Exception {

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
    Item toReturn = null;
    while (rs.next()) {
      String name = rs.getString(2);
      TypeAttribute type = new TypeAttribute(TypeEnum.values()[rs.getInt(3)]);
      FormalityAttribute formality = new FormalityAttribute(
              FormalityEnum.values()[rs.getInt(4)]);

      String colorCSV = rs.getString(5);
      List<Color> colorList = this.parseColorCSV(colorCSV);

      PatternAttribute pattern = new PatternAttribute(
              PatternEnum.values()[rs.getInt(6)]);
      SeasonAttribute season = new SeasonAttribute(
              SeasonEnum.values()[rs.getInt(7)]);

      String image = rs.getString(8);

      toReturn = new Item(id, name, season, formality, pattern,
          new ColorAttribute(colorList.get(0)), type, image);
    }
    rs.close();
    return toReturn;
  }

  public List<ItemProxy> getAllItemsByAttributes(Attribute attributeToQuery,
                                                 List<? extends Attribute>
                                                         attribute,
                                                 Integer userID)
          throws SQLException {
    boolean filterByUser = true;
    if (userID == null) {
      filterByUser = false;
    }

    String attributeName = attributeToQuery.getAttributeName();
    StringBuilder getAllItemsByAttributesSQL;
    int setAttributeStartNum;
    if (filterByUser) {
      //,user_item
      //TODO: change this
      getAllItemsByAttributesSQL = new StringBuilder("SELECT * "
              + "FROM item, user_item "
              + "WHERE "
              + "user_item.item_id=item.id AND "
              + "user_item.user_Id=? AND ("
              + attributeName + "=?");
      setAttributeStartNum = 2;
    } else {
      getAllItemsByAttributesSQL = new StringBuilder("SELECT * "
              + "FROM item "
              + "WHERE "
              + attributeName + "=?");
      setAttributeStartNum = 1;
    }

    for (int i = 1; i < attribute.size(); i++) {
      getAllItemsByAttributesSQL.append(" OR " + attributeName + "=?");
    }
    if (filterByUser) {
      getAllItemsByAttributesSQL.append(")");
    }

    getAllItemsByAttributesSQL.append(";");

    this.getAllItemsByAttributesPrep = this.conn
            .prepareStatement(getAllItemsByAttributesSQL.toString());

    if (filterByUser) {
      this.getAllItemsByAttributesPrep.setInt(1, userID);
    }

    if (attributeName.equals(new ColorAttribute(null).getAttributeName())) {
      for (int i = 0; i < attribute.size(); i++) {
        //this.getAllItemsByAttributesPrep.setString(i, attributeName);
        Color color = (Color) attribute.get(i).getAttributeVal();
        this.getAllItemsByAttributesPrep.setString(setAttributeStartNum + i,
                color.toString());
      }
    } else {
      for (int i = 0; i < attribute.size(); i++) {
        //this.getAllItemsByAttributesPrep.setString(i, attributeName);
        Enum e = (Enum) attribute.get(i).getAttributeVal();
        this.getAllItemsByAttributesPrep.setInt(setAttributeStartNum + i,
                e.ordinal());
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
//  return new Outfit(id, name, itemMap);
    return null;
  }

  /**
   * Called by getUserInfo() to instantiate a User instance.
   *
   * @param id of user
   * @return List of ItemProxy instances
   * @throws SQLException
   */
  public List<ItemProxy> getItemsByUserID(int id) throws SQLException {
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
  public List<OutfitProxy> getOutfitsByUserID(int id) throws SQLException {
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


  public int getOutfitLikes(int id) throws SQLException {
    this.getOutfitLikesPrep.setInt(1, id);
    ResultSet rs = this.getOutfitLikesPrep.executeQuery();
    int likes = 0;
    while (rs.next()) {
      likes = rs.getInt(7);
    }
    rs.close();
    return likes;
  }


  /**
   * Adds a new user.
   *
   * @param name  - The name of the new use.
   * @param email - the user's email.
   * @throws SQLException
   */
  public void addUser(String name, String email, String hashPwd)
          throws SQLException {
    this.addUserPrep.setString(1, name);
    this.addUserPrep.setString(2, email);
    this.addUserPrep.setString(3, hashPwd);
    this.addUserPrep.executeUpdate();
  }

  public int addItem(int userId, String name, TypeAttribute type,
                     FormalityAttribute formality, ColorAttribute color,
                     PatternAttribute pattern, SeasonAttribute season) throws SQLException {
    this.addItemPrep.setString(1, name);
    this.addItemPrep.setInt(2, type.getAttributeVal().ordinal());
    this.addItemPrep.setInt(3, formality.getAttributeVal().ordinal());
    this.addItemPrep.setString(4, color.getAttributeVal().toString());
    this.addItemPrep.setInt(5, pattern.getAttributeVal().ordinal());
    this.addItemPrep.setInt(6, season.getAttributeVal().ordinal());
    this.addItemPrep.setString(7, defaultImageMap.get(type.getAttributeVal().ordinal()));
    this.addItemPrep.executeUpdate();

    ResultSet rs = this.lastInsertID.executeQuery();
    if (rs.next()) {
      int itemID = rs.getInt(1);

      this.addItemToUserPrep.setInt(1, userId);
      this.addItemToUserPrep.setInt(2, itemID);
      this.addItemToUserPrep.executeUpdate();
      return itemID;
    } else {
      throw new SQLException();
    }
  }

  public void incrementLikes(int outfitId) throws SQLException {
    this.incrementLikesPrep.setInt(1, outfitId);
    this.incrementLikesPrep.executeUpdate();
  }

  public void addOutfit(UserProxy userProxy, String outfitName,
                        Map<TypeEnum, ItemProxy> items) throws SQLException {
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
    if (rs.next()) {
      int outfitID = rs.getInt(1);

      this.addOutfitToUserPrep.setInt(1, userProxy.getId());
      this.addOutfitToUserPrep.setInt(2, outfitID);
      this.addOutfitToUserPrep.executeUpdate();
    }
  }

  /**
   * Deletes a user from the user Table, all user item and outfit references
   * from the user_item and user_outfit tables respectively.
   *
   * @param userProxy - the user to be deleted
   * @throws SQLException
   */
  public void deleteUser(UserProxy userProxy) throws SQLException {
    this.deleteUserPrep.setInt(1, userProxy.getId());
    this.deleteUserPrep.executeUpdate();
    this.deleteAllUserItemsPrep.setInt(1, userProxy.getId());
    this.deleteAllUserItemsPrep.executeUpdate();
    this.deleteAllUserOutfitsPrep.setInt(1, userProxy.getId());
    this.deleteAllUserOutfitsPrep.executeUpdate();
  }

  /**
   * Deletes an item from the item table and its reference from the user_item
   * table.
   *
   * @param userId - id of user that owns the item
   * @param itemId - id of item to be deleted
   * @throws SQLException
   */
  public void deleteItem(int userId, int itemId) throws SQLException {
    // TODO: delete item (might be referenced by other users)?
//    deleteItemPrep.setString(1, item.getId());
//    deleteItemPrep.executeUpdate();
    this.deleteUserItemPrep.setInt(1, userId);
    this.deleteUserItemPrep.setInt(2, itemId);
    this.deleteUserItemPrep.executeUpdate();
  }

  /**
   * Deletes an outfit from the outfit table and its reference in the
   * user_outfit table.
   *
   * @param userId   - id of user that owns the item
   * @param outfitId - id of item to be deleted
   * @throws SQLException
   */
  public void deleteOutfit(int userId, int outfitId) throws SQLException {
    // TODO: delete outfit (might be referenced by other users)?
//    deleteOutfitPrep.setString(1, outfit.getId());
//    deleteOutfitPrep.executeUpdate();
    this.deleteUserOutfitPrep.setInt(1, userId);
    this.deleteUserOutfitPrep.setInt(2, outfitId);
    this.deleteUserOutfitPrep.executeUpdate();
  }

}
