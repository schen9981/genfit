package edu.brown.cs.suggestor;

import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.suggester.OutfitSuggester;
import org.junit.Test;

public class OutfitSuggestorTest {
  @Test
  public void testSingleItemOutfit() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      os.suggestOutfits(db, 104);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
