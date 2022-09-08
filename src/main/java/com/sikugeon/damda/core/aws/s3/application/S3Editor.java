package com.sikugeon.damda.core.aws.s3.application;

import java.util.Map;

public interface S3Editor {
    void createBucket(Map<String, String> awsKey, String bucketName);

    void addPolicyToBucket(Map<String, String> awsKey, String bucketName);
}
