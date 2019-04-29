package edu.brown.cs.rankers;

import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Outfit;
import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.proxy.ItemProxy;
import com.genfit.suggester.OutfitSuggester;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SuggestorTest {
  @Test
  public void testBasic() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      Map<TypeEnum, ItemProxy> itemMap = new HashMap<>();
      itemMap.put(TypeEnum.TOP, new ItemProxy(db, 1));

      Outfit o = new Outfit(0, "dummy0", itemMap);
      System.out.println(os.suggestOutfits(o, db, 99));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
