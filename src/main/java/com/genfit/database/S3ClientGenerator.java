package com.genfit.database;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Created by ericwang on 2019/4/28.
 */
public class S3ClientGenerator {
  private static final String bucket_name = "cs32-term-project-s3-bucket";
  private static final String access_key = "AKIAUSTN5WVYFTHPLBSU";
  private static final String secret_key = "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";
  private static final String region = "us-east-1";

  public static AmazonS3 getS3Client() {
    BasicAWSCredentials creds = new BasicAWSCredentials(access_key, secret_key);
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(creds))
            .withRegion(region).build();
    return s3Client;
  }
}
