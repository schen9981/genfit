package edu.brown.cs.suggestor;

import com.genfit.database.AWSConnection;
import com.genfit.database.Database;
import com.genfit.suggester.OutfitSuggester;
import com.genfit.suggester.OutfitSuggestion;
import org.junit.Test;

import java.util.List;

public class OutfitSuggestorTest {
  @Test
  public void testSingleItemOutfit() {
    try {
      Database db = new Database(AWSConnection.getDBConnectionUsingIam());
      OutfitSuggester os = new OutfitSuggester();

      List<OutfitSuggestion> l = os.suggestOutfits(db, 104);

      for (int i = 0; i < l.size(); i++) {
        OutfitSuggestion suggestion = l.get(i);
        System.out.println(suggestion.getReferenceOutfit());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
