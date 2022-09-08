package com.sikugeon.damda.core.aws.s3.application;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;

import java.util.Map;

@Service
public class S3Manager implements S3Editor {

    Logger log = LoggerFactory.getLogger(S3Manager.class);

    public AmazonS3Client S3Client(String accessKey, String secretKey){
        log.debug(accessKey+" : "+secretKey);
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = "ap-northeast-2";
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Override
    public void createBucket(Map<String, String> awsKey, String bucketName) {
        String accessKey = awsKey.get("accessKeyId");
        String secretKey = awsKey.get("secretAccessKey");
        AmazonS3Client amazonS3Client = S3Client(accessKey, secretKey);
        try {
            if (!amazonS3Client.doesBucketExistV2(bucketName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
                amazonS3Client.createBucket(new CreateBucketRequest(bucketName));

                // Verify that the bucket was created by retrieving it and checking its location.
                String bucketLocation = amazonS3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
                log.debug("Bucket location: " + bucketLocation);
            }
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it and returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

    }

    @Override
    public void addPolicyToBucket(Map<String, String> awsKey, String bucketName) {
        String accessKey = awsKey.get("accessKeyId");
        String secretKey = awsKey.get("secretAccessKey");
        AmazonS3Client amazonS3Client = S3Client(accessKey, secretKey);

        String policyText=String.format("{\n" +
                "    \"Version\": \"2012-10-17\",\n" +
                "    \"Id\": \"Policy1658991137911\",\n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Sid\": \"Stmt1658991135793\",\n" +
                "            \"Effect\": \"Allow\",\n" +
                "            \"Principal\": \"*\",\n" +
                "            \"Action\": [\n" +
                "                \"s3:GetObject\",\n" +
                "                \"s3:PutObject\"\n" +
                "            ],\n" +
                "            \"Resource\": \"arn:aws:s3:::%s/*\"\n" +
                "        }\n" +
                "    ]\n" +
                "}",bucketName);
        try{
            amazonS3Client.setBucketPolicy(bucketName, policyText);
        }catch(Exception e){
            throw e;
        }
    }
}
