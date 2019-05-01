package edu.brown.cs.suggestor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;
import com.genfit.suggester.OutfitSuggester;

import static org.junit.Assert.assertEquals;

public class ItemSuggestorTest {
  @Test
  public void testSingleItemOutfit() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
      itemMap.put(TypeEnum.TOP, new ItemProxy(db, 61));

      Outfit o = new Outfit(0, "dummy0", itemMap);

      Map<TypeEnum, List<ItemProxy>> items = os.suggestItems(o, db, 104);

//      assertEquals(11, items.size());

      // List<Integer> itemIDs = new ArrayList<>(items.size());
      // for (int i = 0; i < items.size(); i++) {
      // ItemProxy itemProxy = items.get(i);
      // itemIDs.add(itemProxy.getId());
      // }
      //
      //
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMultiItemOutfit() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
      itemMap.put(TypeEnum.TOP, new ItemProxy(db, 61));
      itemMap.put(TypeEnum.BOTTOM, new ItemProxy(db, 48));

      Outfit o = new Outfit(0, "dummy0", itemMap);

      Map<TypeEnum, List<ItemProxy>> items = os.suggestItems(o, db, 104);
      assertEquals(7, items.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
