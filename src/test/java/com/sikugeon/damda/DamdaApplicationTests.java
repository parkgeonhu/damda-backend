package com.sikugeon.damda;

import com.sikugeon.damda.common.util.RandomUtils;
import com.sikugeon.damda.core.aws.iam.application.IamManager;
import com.sikugeon.damda.core.aws.s3.application.S3Manager;
import com.sikugeon.damda.core.object.application.S3Uploader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.location=" +
        "classpath:application.yml" +
        ",classpath:secret.yml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DamdaApplicationTests {

    Logger log = LoggerFactory.getLogger(DamdaApplicationTests.class);

    @Autowired
    S3Manager s3Manager;
    @Autowired
    S3Uploader s3Uploader;
    @Autowired
    IamManager iamManager;

    List<MultipartFile> multipartFiles;

    @BeforeAll
    public void setUp() throws IOException {
        multipartFiles = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/test.png");
        multipartFiles.add(new MockMultipartFile("test.png", fileInputStream));
    }

    @Test
    void IAM유저생성_후_S3버킷생성_후_S3업로드() throws InterruptedException {
        String username = RandomUtils.randomString();
        final String GROUP_NAME = "damda";

        iamManager.createIAMUser(username);
        Map<String, String> userSession = iamManager.createIAMAccessKey(username);
        boolean result = iamManager.addUserToGroup(username, GROUP_NAME);
        assertThat(result).isTrue();

        Thread.sleep(15000);

        String bucketName = RandomUtils.randomString();

        String accessKeyId = userSession.get("accessKeyId");
        String secretAccessKey = userSession.get("secretAccessKey");
        s3Manager.createBucket(accessKeyId, secretAccessKey, bucketName);
        s3Manager.addPolicyToBucket(accessKeyId, secretAccessKey, bucketName);

        log.debug(String.valueOf(s3Uploader.upload(userSession, multipartFiles, bucketName, "app")));
    }

}
