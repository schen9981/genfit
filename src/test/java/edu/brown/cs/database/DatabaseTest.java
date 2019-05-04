package edu.brown.cs.database;

import com.genfit.database.AWSConnection;
import com.genfit.database.Database;

import java.sql.Connection;

public class DatabaseTest {
  private static final String bucket_name = "cs32-term-project-s3-bucket";
  private static final String access_key = "AKIAUSTN5WVYFTHPLBSU";
  private static final String secret_key = "uetUyr0W+vuI"
          + "+iFZBl6HCWB9kgYNoXgYPTX6kCei";
  private static final String region = "us-east-1";

  public static void main(String[] args) throws Exception {
    Connection connection = AWSConnection.getDBConnectionUsingIam();
    Database db = new Database(connection);


    //db.editItem(103, 95,
    //        new TypeAttribute(TypeEnum.values()[1]),
    //        new SubtypeAttribute(SubtypeEnum.values()[1]),
    //        new FormalityAttribute(FormalityEnum.values()[1]),
    //        new ColorAttribute(new Color(0x111111)),
    //        new PatternAttribute(PatternEnum.values()[1]),
    //        new SeasonAttribute(SeasonEnum.values()[1]));

//      db.addItem(103, "someshirt",
//              new TypeAttribute(TypeEnum.TOP),
//              new FormalityAttribute(FormalityEnum.CASUAL),
//              new ColorAttribute(new Color(0xffffff)),
//              new PatternAttribute(PatternEnum.SOLID),
//              new SeasonAttribute(SeasonEnum.SPRING),
//              "default",
//              new SubtypeAttribute(SubtypeEnum.T_SHIRT));


//      Item item = db.getItemBean(173);
//      System.out.println(item.getType());
//      System.out.println(item.getSubtype());


//    UserProxy user = new UserProxy(db, "wenhuang_zeng@brown.edu");
//    OutfitProxy outfitProxy = new OutfitProxy(db, 16);
//    db.deleteOutfit(0, 2);
    connection.close();
  }
}
