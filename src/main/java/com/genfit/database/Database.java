package com.genfit.database;

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
import com.genfit.attribute.SubtypeAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.SubtypeEnum;
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

public class Database {
  // Check statements
  private final String checkLoginSQL = "SELECT * FROM user WHERE email = ?;";
  private final String checkSignupSQL = "SELECT * FROM user WHERE email = ?;";
  private final String checkOutfitWithItemsSQL = "SELECT * FROM outfit as a JOIN user_outfit as b WHERE a.id = b.outfit_id AND "
      + "b.user_id = ? AND a.outer = ? AND a.top = ? AND a.bottom = ? AND a.feet = ?;";
  // Get Statements
  private final String getUserInfoSQL = "SELECT * FROM user WHERE email=?;";
  private final String getItemInfoSQL = "SELECT * FROM item WHERE id=?;";
  private final String getOutfitInfoSQL = "SELECT * FROM outfit WHERE id=?;";
  private final String getOutfitsExcludeUserSQL = "SELECT outfit_id FROM "
      + "user_outfit " + "WHERE " + "user_id!=?;";
  private final String getItemsByUserIDSQL = "SELECT * FROM user_item WHERE "
      + "user_id=?;";
  private final String getOutfitsByUserIDSQL = "SELECT * FROM user_outfit "
      + "WHERE user_id=?;";

  private final String getOutfitLikesSQL = "SELECT * FROM outfit WHERE id=?;";
  private final String getLikedOutfitIdsSQL = "SELECT * FROM user_liked WHERE"
      + " user_id=?";
  private final String getOutfitsWithItemIdSQL = "SELECT * FROM outfit as a JOIN user_outfit as b WHERE a.id = b.outfit_id AND b.user_id = ? AND (a.outer = ? " + "OR a.top = ? OR a.bottom = ? OR a.feet = ?);";

  // Add Statements
  private final String addUserSQL = "INSERT INTO user (name, email, password)"
      + " values (?, ?, ?);";
  private final String addItemSQL = "INSERT IGNORE INTO item"
      + " (name, type, formality, color, pattern, season, image, subtype)"
      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
  private final String addItemToUserSQL = "INSERT INTO user_item "
      + "(user_id, item_id) VALUES (?, ?);";
  private final String addOutfitSQL = "INSERT IGNORE INTO outfit"
      + " (name, `outer`, top, bottom, feet) VALUES (?, ?, ?, ?, ?);";
  private final String addOutfitToUserSQL = "INSERT INTO user_outfit"
      + " (user_id, outfit_id) VALUES (?, ?);";
  // edit Statements
  private final String editItemSQL = "UPDATE item SET type=?, subtype=?, formality=?, color=?, pattern=?,season=? WHERE id=?;";
  // Delete Statements
  private final String deleteUserSQL = "DELETE FROM user WHERE id=?;";
  private final String deleteAllUserItemsSQL = "DELETE FROM user_item WHERE "
      + "user_id=?;";
  private final String deleteAllUserOutfitsSQL = "DELETE FROM user_outfit "
      + "WHERE user_id=?;";
  private final String incrementLikesSQL = "UPDATE outfit SET likes = "
      + "(likes + ?) WHERE id = ?; INSERT INTO user_liked (user_id, "
      + "outfit_id) " + "VALUES (?, ?);";
  private final String decrementLikesSQL = "UPDATE outfit SET likes = "
      + "(likes + ?) WHERE id = ?; DELETE FROM user_liked WHERE (user_id "
      + "= ?) " + "and (outfit_id = ?);";
  private final String deleteItemSQL = "DELETE FROM item WHERE id=?;";
  private final String deleteUserItemSQL = "DELETE FROM user_item WHERE "
      + "user_id=? AND item_id=?;";
  private final String deleteOutfitSQL = "DELETE FROM outfit WHERE id=?;";
  private final String deleteUserOutfitSQL = "DELETE FROM user_outfit WHERE "
      + "user_id=? AND outfit_id=?;";
  // Misc Statements
  private final String lastInsertIDSQL = "SELECT LAST_INSERT_ID();";
  private final String changePasswordSQL = "UPDATE user SET password = ? "
      + "WHERE email = ?";
  private PreparedStatement getUserInfoPrep, getItemInfoPrep, getOutfitInfoPrep;
  private PreparedStatement getItemsByUserIDPrep, getOutfitsByUserIDPrep;
  private PreparedStatement getAllItemsByAttributesPrep,
      getOutfitsWithItemIdPrep;
  private PreparedStatement getOutfitLikesPrep, getLikedOutfitIdsPrep;
  private PreparedStatement deleteUserPrep, deleteAllUserItemsPrep,
      deleteAllUserOutfitsPrep, incrementLikesPrep, decrementLikesPrep;
  private Connection conn = null;
  private PreparedStatement addUserPrep;
  private PreparedStatement addItemPrep, addItemToUserPrep;
  private PreparedStatement addOutfitPrep, addOutfitToUserPrep;
  private PreparedStatement editItemPrep;
  private PreparedStatement deleteItemPrep, deleteUserItemPrep;
  private PreparedStatement deleteOutfitPrep, deleteUserOutfitPrep;
  private PreparedStatement changePasswordPrep;
  private PreparedStatement lastInsertID;
  private LoadingCache<String, User> userCache;
  private LoadingCache<Integer, Item> itemCache;
  private LoadingCache<Integer, Outfit> outfitCache;
  private PreparedStatement checkLoginPrep, checkSignupPrep,
      checkOutfitWithItemsPrep;
  private PreparedStatement getOutfitsExcludeUserPrep;

  private Map<Integer, String> defaultImageMap = new HashMap<>();

  public Database(Connection conn) {
    try {
      this.conn = conn;
      Statement stmt = conn.createStatement();
      stmt.execute("USE genfit;");
      this.changePasswordPrep = conn.prepareStatement(this.changePasswordSQL);
      this.checkLoginPrep = conn.prepareStatement(this.checkLoginSQL);
      this.checkSignupPrep = conn.prepareStatement(this.checkSignupSQL);
      this.checkOutfitWithItemsPrep = conn
          .prepareStatement(this.checkOutfitWithItemsSQL);
      this.getUserInfoPrep = conn.prepareStatement(this.getUserInfoSQL);
      this.getItemInfoPrep = conn.prepareStatement(this.getItemInfoSQL);
      this.getOutfitInfoPrep = conn.prepareStatement(this.getOutfitInfoSQL);
      this.getOutfitsExcludeUserPrep = conn
          .prepareStatement(this.getOutfitsExcludeUserSQL);
      this.getItemsByUserIDPrep = conn
          .prepareStatement(this.getItemsByUserIDSQL);
      this.getOutfitsByUserIDPrep = conn
          .prepareStatement(this.getOutfitsByUserIDSQL);
      this.getOutfitLikesPrep = conn.prepareStatement(this.getOutfitLikesSQL);
      this.getLikedOutfitIdsPrep = conn
          .prepareStatement(this.getLikedOutfitIdsSQL);
      this.getOutfitsWithItemIdPrep = conn
          .prepareStatement(this.getOutfitsWithItemIdSQL);

      this.addUserPrep = conn.prepareStatement(this.addUserSQL);
      this.addItemPrep = conn.prepareStatement(this.addItemSQL);
      this.addItemToUserPrep = conn.prepareStatement(this.addItemToUserSQL);
      this.addOutfitPrep = conn.prepareStatement(this.addOutfitSQL);
      this.addOutfitToUserPrep = conn.prepareStatement(this.addOutfitToUserSQL);
      this.incrementLikesPrep = conn.prepareStatement(this.incrementLikesSQL);
      this.decrementLikesPrep = conn.prepareStatement(this.decrementLikesSQL);

      this.editItemPrep = conn.prepareStatement(this.editItemSQL);

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

      this.defaultImageMap.put(TypeEnum.OUTER.ordinal(),
          "https://s3.amazonaws.com/cs32-term-project-s3-bucket"
              + "/default/outer_jacket.png");
      this.defaultImageMap.put(TypeEnum.TOP.ordinal(),
          "https://s3.amazonaws.com/cs32-term-project-s3-bucket/default"
              + "/tshirt" + ".png");
      this.defaultImageMap.put(TypeEnum.BOTTOM.ordinal(),
          "https://s3.amazonaws.com/cs32-term-project-s3-bucket/default"
              + "/pants.png");
      this.defaultImageMap.put(TypeEnum.SHOES.ordinal(),
          "https://s3.amazonaws.com/cs32-term-project-s3-bucket/default"
              + "/sneakers" + ".png");
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
  private synchronized List<Color> parseColorCSV(String colors) {
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
  private synchronized void instantiateCacheLoader() {
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

  public void removeFromItemCache(int id) {
    this.itemCache.invalidate(id);
  }

  /**
   * Called by UserProxy instances.
   *
   * @param email of user
   * @return User instance
   * @throws Exception
   */
  public synchronized User getUserBean(String email) throws Exception {
    return this.userCache.get(email);
  }

  /**
   * Called by ItemProxy instances.
   *
   * @param id of item
   * @return Item instance
   * @throws Exception
   */
  public synchronized Item getItemBean(int id) throws Exception {
    return this.itemCache.get(id);
  }

  /**
   * Called by OutfitProxy instances.
   *
   * @param id oof outfit
   * @return Outfit instance
   * @throws Exception
   */
  public synchronized Outfit getOutfitBean(int id) throws Exception {
    return this.outfitCache.get(id);
  }

  public synchronized void changePassword(String email, String newPwdHash)
      throws Exception {
    this.changePasswordPrep.setString(1, newPwdHash);
    this.changePasswordPrep.setString(2, email);
    this.changePasswordPrep.executeUpdate();
  }

  public synchronized boolean checkLogin(String username, String clientHashPwd)
      throws Exception {
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

  public synchronized boolean checkOutfitWithItems(int id, int outerId,
      int topId, int bottomId, int feetId) throws SQLException {

    this.checkOutfitWithItemsPrep.setInt(1, id);
    this.checkOutfitWithItemsPrep.setInt(2, outerId);
    this.checkOutfitWithItemsPrep.setInt(3, topId);
    this.checkOutfitWithItemsPrep.setInt(4, bottomId);
    this.checkOutfitWithItemsPrep.setInt(5, feetId);
    ResultSet rs = this.checkOutfitWithItemsPrep.executeQuery();
    boolean exist = false;
    if (rs.next()) {
      exist = true;
    }
    rs.close();
    return exist;
  }

  public synchronized boolean checkSignup(String username) throws Exception {

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
  private synchronized User getUserInfo(String email) throws SQLException {
    this.getUserInfoPrep.setString(1, email);
    ResultSet rs = this.getUserInfoPrep.executeQuery();
    int id = 0;
    String name = "";
    while (rs.next()) {
      id = rs.getInt(1);
      name = rs.getString(2);
    }
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
  private synchronized Item getItemInfo(int id) throws SQLException {
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
      SubtypeAttribute subtype = new SubtypeAttribute(
          SubtypeEnum.values()[rs.getInt(9)]);

      toReturn = new Item(id, name, season, formality, pattern,
          new ColorAttribute(colorList.get(0)), type, subtype, image);
    }
    rs.close();
    return toReturn;
  }

  public synchronized List<ItemProxy> getAllItemsByAttributes(
      Attribute attributeToQuery, List<? extends Attribute> attribute,
      Integer userID) throws SQLException {
    boolean filterByUser = true;
    if (userID == null) {
      filterByUser = false;
    }

    String attributeName = attributeToQuery.getAttributeName();
    StringBuilder getAllItemsByAttributesSQL;
    int setAttributeStartNum;
    if (filterByUser) {
      // ,user_item
      // TODO: change this
      getAllItemsByAttributesSQL = new StringBuilder(
          "SELECT * " + "FROM item, user_item " + "WHERE "
              + "user_item.item_id=item.id AND " + "user_item"
              + ".user_Id=? AND (" + attributeName + "=?");
      setAttributeStartNum = 2;
    } else {
      getAllItemsByAttributesSQL = new StringBuilder(
          "SELECT * " + "FROM item " + "WHERE " + attributeName + "=?");
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
        // this.getAllItemsByAttributesPrep.setString(i, attributeName);
        Color color = (Color) attribute.get(i).getAttributeVal();
        this.getAllItemsByAttributesPrep.setString(setAttributeStartNum + i,
            color.toString());
      }
    } else {
      for (int i = 0; i < attribute.size(); i++) {
        // this.getAllItemsByAttributesPrep.setString(i, attributeName);
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
  private synchronized Outfit getOutfitInfo(int id) throws SQLException {
    this.getOutfitInfoPrep.setInt(1, id);
    ResultSet rs = this.getOutfitInfoPrep.executeQuery();

    Outfit toReturn = null;
    while (rs.next()) {

      String name = rs.getString(2);
      int outerID = rs.getInt(3);
      int topID = rs.getInt(4);
      int bottomID = rs.getInt(5);
      int feetID = rs.getInt(6);

      // TODO: URGENT will probably not work for outfits that are incomplete

      ItemProxy outer = new ItemProxy(this, outerID);
      ItemProxy top = new ItemProxy(this, topID);
      ItemProxy bottom = new ItemProxy(this, bottomID);
      ItemProxy feet = new ItemProxy(this, feetID);

      Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
      itemMap.put(TypeEnum.OUTER, outer);
      itemMap.put(TypeEnum.TOP, top);
      itemMap.put(TypeEnum.BOTTOM, bottom);
      itemMap.put(TypeEnum.SHOES, feet);

      toReturn = new Outfit(id, name, itemMap);
    }
    rs.close();
    return toReturn;
  }

  /**
   * Called by getUserInfo() to instantiate a User instance.
   *
   * @param id of user
   * @return List of ItemProxy instances
   * @throws SQLException
   */
  public synchronized List<ItemProxy> getItemsByUserID(int id)
      throws SQLException {
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
  public synchronized List<OutfitProxy> getOutfitsByUserID(int id)
      throws SQLException {
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

  public synchronized int getOutfitLikes(int id) throws SQLException {
    int likes = 0;
    this.getOutfitLikesPrep.setInt(1, id);
    ResultSet rs = this.getOutfitLikesPrep.executeQuery();
    while (rs.next()) {
      likes = rs.getInt(7);
    }
    rs.close();
    return likes;
  }

  public synchronized List<Integer> getLikedOutfitIds(int userId)
      throws SQLException {
    this.getLikedOutfitIdsPrep.setInt(1, userId);
    ResultSet rs = this.getLikedOutfitIdsPrep.executeQuery();
    List<Integer> outfitIds = new ArrayList<>();
    while (rs.next()) {
      outfitIds.add(rs.getInt(2));
    }
    rs.close();
    return outfitIds;
  }

  /**
   * Gets all the outfits in database except those belonging to a certain user.
   *
   * @param id user ID to exclude
   * @return List of OutfitProxy instances
   * @throws SQLException
   */
  public synchronized List<OutfitProxy> getOutfitsExcludeUser(int id)
      throws SQLException {
    List<OutfitProxy> outfitProxyList = new ArrayList<>();
    this.getOutfitsExcludeUserPrep.setInt(1, id);
    ResultSet rs = this.getOutfitsExcludeUserPrep.executeQuery();
    while (rs.next()) {
      int outfitID = rs.getInt(1);
      outfitProxyList.add(new OutfitProxy(this, outfitID));
    }
    rs.close();
    return outfitProxyList;
  }

  /**
   * Adds a new user.
   *
   * @param name  - The name of the new use.
   * @param email - the user's email.
   * @throws SQLException
   */
  public synchronized void addUser(String name, String email, String hashPwd)
      throws SQLException {
    this.addUserPrep.setString(1, name);
    this.addUserPrep.setString(2, email);
    this.addUserPrep.setString(3, hashPwd);
    this.addUserPrep.executeUpdate();
  }

  public synchronized int addItem(int userId, String name, TypeAttribute type,
      FormalityAttribute formality, ColorAttribute color,
      PatternAttribute pattern, SeasonAttribute season, String imageKey,
      SubtypeAttribute subtype) throws SQLException {
    this.addItemPrep.setString(1, name);
    this.addItemPrep.setInt(2, type.getAttributeVal().ordinal());
    this.addItemPrep.setInt(3, formality.getAttributeVal().ordinal());
    this.addItemPrep.setString(4, color.getAttributeVal().toString());
    this.addItemPrep.setInt(5, pattern.getAttributeVal().ordinal());
    this.addItemPrep.setInt(6, season.getAttributeVal().ordinal());
    if (imageKey.equals("default")) {
      this.addItemPrep.setString(7,
          this.defaultImageMap.get(type.getAttributeVal().ordinal()));
    } else {
      this.addItemPrep.setString(7, S3Connection.getUrlPrefix() + imageKey);
    }
    this.addItemPrep.setInt(8, subtype.getAttributeVal().ordinal());
    this.addItemPrep.executeUpdate();

    ResultSet rs = this.lastInsertID.executeQuery();
    if (rs.next()) {
      int itemID = rs.getInt(1);

      this.addItemToUserPrep.setInt(1, userId);
      this.addItemToUserPrep.setInt(2, itemID);
      this.addItemToUserPrep.executeUpdate();
      rs.close();
      return itemID;
    } else {
      throw new SQLException();
    }
  }

  public synchronized void changeLikes(int outfitId, int userId, int change)
      throws SQLException {
    if (change == -1) {
      this.decrementLikesPrep.setInt(1, change);
      this.decrementLikesPrep.setInt(2, outfitId);
      this.decrementLikesPrep.setInt(3, userId);
      this.decrementLikesPrep.setInt(4, outfitId);
      this.decrementLikesPrep.executeUpdate();
    } else if (change == 1) {
      this.incrementLikesPrep.setInt(1, change);
      this.incrementLikesPrep.setInt(2, outfitId);
      this.incrementLikesPrep.setInt(3, userId);
      this.incrementLikesPrep.setInt(4, outfitId);
      this.incrementLikesPrep.executeUpdate();
    }

  }

  public synchronized int addOutfit(int userId, String outfitName,
      Map<TypeEnum, Integer> items) throws SQLException {

    int outerId = items.get(TypeEnum.OUTER);
    int topId = items.get(TypeEnum.TOP);
    int bottomId = items.get(TypeEnum.BOTTOM);
    int shoeId = items.get(TypeEnum.SHOES);

    this.addOutfitPrep.setString(1, outfitName);
    this.addOutfitPrep.setInt(2, outerId);
    this.addOutfitPrep.setInt(3, topId);
    this.addOutfitPrep.setInt(4, bottomId);
    this.addOutfitPrep.setInt(5, shoeId);
    this.addOutfitPrep.executeUpdate();

    ResultSet rs = this.lastInsertID.executeQuery();
    if (rs.next()) {
      int outfitID = rs.getInt(1);

      this.addOutfitToUserPrep.setInt(1, userId);
      this.addOutfitToUserPrep.setInt(2, outfitID);
      this.addOutfitToUserPrep.executeUpdate();
      rs.close();
      return outfitID;
    } else {
      rs.close();
      throw new SQLException();
    }
  }

  public synchronized void editItem(int itemId, TypeAttribute type1,
      SubtypeAttribute type2, FormalityAttribute formality,
      ColorAttribute color, PatternAttribute pattern, SeasonAttribute season)
      throws SQLException {
    this.editItemPrep.setInt(1, type1.getAttributeVal().ordinal());
    this.editItemPrep.setInt(2, type2.getAttributeVal().ordinal());
    this.editItemPrep.setInt(3, formality.getAttributeVal().ordinal());
    this.editItemPrep.setString(4, color.getAttributeVal().toString());
    this.editItemPrep.setInt(5, pattern.getAttributeVal().ordinal());
    this.editItemPrep.setInt(6, season.getAttributeVal().ordinal());
    this.editItemPrep.setInt(7, itemId);
    this.editItemPrep.executeUpdate();
  }

  /**
   * Deletes a user from the user Table, all user item and outfit references
   * from the user_item and user_outfit tables respectively.
   *
   * @param userProxy - the user to be deleted
   * @throws SQLException
   */
  public synchronized void deleteUser(UserProxy userProxy) throws SQLException {
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
  public synchronized void deleteItem(int userId, int itemId)
      throws SQLException {
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
  public synchronized void deleteOutfit(int userId, int outfitId)
      throws SQLException {
    // TODO: delete outfit (might be referenced by other users)?
//    deleteOutfitPrep.setString(1, outfit.getId());
//    deleteOutfitPrep.executeUpdate();
    this.deleteUserOutfitPrep.setInt(1, userId);
    this.deleteUserOutfitPrep.setInt(2, outfitId);
    this.deleteUserOutfitPrep.executeUpdate();
  }

  public synchronized List<Integer> getOutfitsWithItemId(int userId, int itemId)
      throws SQLException {

    this.getOutfitsWithItemIdPrep.setInt(1, userId);
    this.getOutfitsWithItemIdPrep.setInt(2, itemId);
    this.getOutfitsWithItemIdPrep.setInt(3, itemId);
    this.getOutfitsWithItemIdPrep.setInt(4, itemId);
    this.getOutfitsWithItemIdPrep.setInt(5, itemId);
    List<Integer> outfitIds = new ArrayList<>();
    ResultSet rs = this.getOutfitsWithItemIdPrep.executeQuery();
    while (rs.next()) {
      outfitIds.add(rs.getInt(1));
    }
    rs.close();
    return outfitIds;
  }

  public void closeConnection() {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

}
