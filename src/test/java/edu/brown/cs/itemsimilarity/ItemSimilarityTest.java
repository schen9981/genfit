package edu.brown.cs.itemsimilarity;

import com.genfit.attribute.attributevals.Color;
import com.genfit.clothing.ItemDistanceCalculator;
import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;
import org.junit.Test;

public class ItemSimilarityTest {
  @Test
  public void testAttributeDistance() {
    Database db = null;
    try {
      db = new Database(AWSConnection.getDBConnectionUsingIam());
      System.out.println(new Color(128, 130, 104).getLABDistance(
              new Color(183, 193, 58)));

      //System.out.println(ItemDistanceCalculator.getSimilarity(
      //        new ItemProxy(db, 107),
      //        new ItemProxy(db, 47)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
