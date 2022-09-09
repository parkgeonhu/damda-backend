package com.sikugeon.damda.core.aws.s3.application;

import java.util.List;
import java.util.Map;

public interface S3Finder {
    List getObjects(Map<String, String> awsKey, String bucketName, String prefix);
}
