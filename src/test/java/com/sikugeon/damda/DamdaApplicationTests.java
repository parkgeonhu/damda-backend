package com.sikugeon.damda;

import com.sikugeon.damda.common.util.RandomUtils;
import com.sikugeon.damda.core.aws.iam.application.IamManager;
import com.sikugeon.damda.core.aws.s3.application.S3Manager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.location=" +
        "classpath:application.yml" +
        ",classpath:aws.yml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DamdaApplicationTests {

    Logger log = LoggerFactory.getLogger(DamdaApplicationTests.class);

    @Autowired
    S3Manager s3Manager;
    @Autowired
    IamManager iamManager;

    @Test
    void IAM유저생성_후_S3버킷생성() throws InterruptedException {
        String username = RandomUtils.randomString();
        final String GROUP_NAME = "damda";

        iamManager.createIAMUser(username);
        Map<String, String> map = iamManager.createIAMAccessKey(username);
        boolean result = iamManager.addUsertoGroup(username, GROUP_NAME);
        assertThat(result).isTrue();
        log.debug(String.valueOf(map));

        Thread.sleep(15000);

        String bucketName = RandomUtils.randomString();

        s3Manager.createBucket(map.get("accessKeyId"), map.get("secretAccessKey"), bucketName);
        s3Manager.addPolicyToBucket(map.get("accessKeyId"), map.get("secretAccessKey"), bucketName);

    }

}
