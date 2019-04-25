package com.genfit.database;

import com.genfit.clothing.Item;
import com.genfit.clothing.Outfit;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.proxy.UserProxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static com.genfit.database.IAMDatabaseAuthenticationTester.getDBConnectionUsingIam;

/**
 * Created by ericwang on 2019/4/24.
 */
public class DatabaseTest {

  public static void main(String[] args) throws Exception {
    //get the connection
    Connection connection = getDBConnectionUsingIam();

    //verify the connection is successful
    Statement stmt = connection.createStatement();
    stmt.execute("USE genfit;");
    stmt.close();
    Database db = new Database(connection);

    UserProxy up = new UserProxy(db, "zero@gmail.com");
    for (ItemProxy i: up.getItems()) {
      System.out.println(i.getId());
    }
    List<OutfitProxy> outfitProxyList = up.getOutfits();
    for (OutfitProxy op : outfitProxyList) {
      System.out.println(op.getId());
    }

//    db.addItem(1, "dbtestitem");
    //db.deleteItem(up, new ItemProxy(db, 8));
    //close the connection
    connection.close();
  }

}
