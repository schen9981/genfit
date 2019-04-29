package com.genfit.database;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.genfit.proxy.ItemProxy;
import com.genfit.proxy.OutfitProxy;
import com.genfit.proxy.UserProxy;

import java.io.File;
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
    BasicAWSCredentials creds = new BasicAWSCredentials(access_key, secret_key);
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(creds))
            .withRegion(region).build();
    String filePath = "/Users/ericwang/Desktop/shirt.jpg";
    String[] splitFilePath = filePath.split("/");
    String keyName = splitFilePath[splitFilePath.length - 1];
    try {
      //s3Client.putObject(bucket_name, keyName, new File(filePath));
    } catch (AmazonServiceException e) {
      System.err.println(e.getErrorMessage());
      System.exit(1);
    }

    ListObjectsV2Result result = s3Client.listObjectsV2(bucket_name);
    List<S3ObjectSummary> objects = result.getObjectSummaries();
    for (S3ObjectSummary os: objects) {
      System.out.println("* " + os.getKey());
    }
    System.out.println("Done");
//    //get the connection
//    Connection connection = getDBConnectionUsingIam();
//
//    //verify the connection is successful
//    Statement stmt = connection.createStatement();
//    stmt.execute("USE genfit;");
//    stmt.close();
//    Database db = new Database(connection);
//
//    UserProxy up = new UserProxy(db, "zero@gmail.com");
//    for (ItemProxy i: up.getItems()) {
//      System.out.println(i.getId());
//    }
//    List<OutfitProxy> outfitProxyList = up.getOutfits();
//    for (OutfitProxy op : outfitProxyList) {
//      System.out.println(op.getId());
//    }

//    db.addItem(1, "dbtestitem");
    //db.deleteItem(up, new ItemProxy(db, 8));
    //close the connection
//    connection.close();
  }

}
