package com.genfit.database;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Class containing connection information for the S3 image storage directory.
 */
public class S3Connection {
  private static final String bucketName = "cs32-term-project-s3-bucket";
  private static final String access_key = "AKIAUSTN5WVYFTHPLBSU";
  private static final String secret_key = "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";
  private static final String region = "us-east-1";

  private static final String urlPrefix = "https://s3.amazonaws.com/cs32-term-project-s3-bucket/";

  public static AmazonS3 getS3Client() {
    BasicAWSCredentials creds = new BasicAWSCredentials(access_key, secret_key);
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(creds))
            .withRegion(region).build();
    return s3Client;
  }

  public static String getBucketName() {
    return bucketName;
  }

  public static String getUrlPrefix() {
    return urlPrefix;
  }
}
