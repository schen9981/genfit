package edu.brown.cs.rankers;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;
import com.genfit.suggester.OutfitSuggester;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestorTest {
  @Test
  public void testBasic() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
      itemMap.put(TypeEnum.TOP, new ItemProxy(db, 26));

      Outfit o = new Outfit(0, "dummy0", itemMap);

      List<ItemProxy> items = os.suggestItems(o, db, 101);
      for (int i = 0; i < items.size(); i++) {
        ItemProxy itemProxy = items.get(i);
        System.out.println(itemProxy.getId());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
