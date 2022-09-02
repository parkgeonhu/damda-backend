package com.sikugeon.damda.core.aws.s3.application;

public interface S3Editor {
    void createBucket(String accessKey, String secretKey, String bucketName);

    void addPolicyToBucket(String accessKey, String secretKey, String bucketName);
}
