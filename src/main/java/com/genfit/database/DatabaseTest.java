package com.genfit.database;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.genfit.attribute.ColorAttribute;
import com.genfit.attribute.FormalityAttribute;
import com.genfit.attribute.PatternAttribute;
import com.genfit.attribute.SeasonAttribute;
import com.genfit.attribute.SubtypeAttribute;
import com.genfit.attribute.TypeAttribute;
import com.genfit.attribute.attributevals.Color;
import com.genfit.attribute.attributevals.FormalityEnum;
import com.genfit.attribute.attributevals.PatternEnum;
import com.genfit.attribute.attributevals.SeasonEnum;
import com.genfit.attribute.attributevals.SubtypeEnum;
import com.genfit.attribute.attributevals.TypeEnum;
import com.genfit.clothing.Item;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.proxy.UserProxy;
import com.genfit.users.User;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static com.genfit.database.AWSConnection.getDBConnectionUsingIam;

/**
 * Created by ericwang on 2019/4/24.
 */
public class DatabaseTest {
  private static final String bucket_name = "cs32-term-project-s3-bucket";
  private static final String access_key = "AKIAUSTN5WVYFTHPLBSU";
  private static final String secret_key = "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";
  private static final String region = "us-east-1";

  public static void main(String[] args) throws Exception {
      Connection connection = AWSConnection.getDBConnectionUsingIam();
      Database db = new Database(connection);
//      db.addItem(103, "someshirt",
//              new TypeAttribute(TypeEnum.TOP),
//              new FormalityAttribute(FormalityEnum.CASUAL),
//              new ColorAttribute(new Color(0xffffff)),
//              new PatternAttribute(PatternEnum.SOLID),
//              new SeasonAttribute(SeasonEnum.SPRING),
//              "default",
//              new SubtypeAttribute(SubtypeEnum.T_SHIRT));
      Item item = db.getItemBean(173);
      System.out.println(item.getType());
      System.out.println(item.getSubtype());
//    UserProxy user = new UserProxy(db, "wenhuang_zeng@brown.edu");
//    OutfitProxy outfitProxy = new OutfitProxy(db, 16);
//    db.deleteOutfit(0, 2);
    connection.close();
  }

}
