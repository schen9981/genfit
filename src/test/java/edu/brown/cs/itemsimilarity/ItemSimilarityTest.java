package edu.brown.cs.itemsimilarity;

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
      //System.out.println(new Color(128, 130, 104).getLABDistance(
      //        new Color(183, 193, 58)));

      System.out.println("boat shoes and converses");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 55),
              new ItemProxy(db, 56)));

      System.out.println("boat shoes and sandals");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 55),
              new ItemProxy(db, 57)));

      System.out.println("converses and sandals");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 56),
              new ItemProxy(db, 57)));

      System.out.println("two different basketball shoes (red and black)");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 122),
              new ItemProxy(db, 101)));

      System.out.println("two different basketball shoes (red and black)");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 122),
              new ItemProxy(db, 101)));

      System.out.println("converse and red tshirt");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 56),
              new ItemProxy(db, 58)));

      System.out.println("red shoes and red tshirt");
      System.out.println(ItemDistanceCalculator.getSimilarity(
              new ItemProxy(db, 122),
              new ItemProxy(db, 58)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
