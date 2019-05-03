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

      System.out.println("boat shoes and converses");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 55),
              new ItemProxy(db, 56)));

      System.out.println("boat shoes and sandals");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 55),
              new ItemProxy(db, 57)));

      System.out.println("converses and sandals");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 56),
              new ItemProxy(db, 57)));

      System.out.println("two different basketball shoes (red and black)");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 122),
              new ItemProxy(db, 101)));

      System.out.println("two different basketball shoes (red and black)");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 122),
              new ItemProxy(db, 101)));

      System.out.println("converse and red tshirt");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 56),
              new ItemProxy(db, 58)));

      System.out.println("red shoes and red tshirt");
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 122),
              new ItemProxy(db, 58)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSpecific() {
    Database db = null;
    try {
      db = new Database(AWSConnection.getDBConnectionUsingIam());
      System.out.println(ItemDistanceCalculator.areSimilar(
              new ItemProxy(db, 184),
              new ItemProxy(db, 196)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
