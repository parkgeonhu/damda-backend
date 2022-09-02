package com.sikugeon.damda.core.iam.application;

import com.sikugeon.damda.common.util.RandomUtils;
import net.bytebuddy.utility.RandomString;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import java.util.Map;

@SpringBootTest(properties = {"spring.config.location=" +
        "classpath:application.yml" +
        ",classpath:aws.yml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IamManagerTest {
    @Autowired
    IamManager iamManager;

    String username="";
    final String GROUP_NAME = "damda";

    @BeforeAll
    public void setUp(){
        this.username = RandomUtils.randomString();
    }

    @Test
    @Order(1)
    void testRegisterIamUser(){
        iamManager.createIAMUser(username);
        Map map = iamManager.createIAMAccessKey(username);
        boolean result = iamManager.addUsertoGroup(username, GROUP_NAME);
        assertThat(result).isTrue();
    }
}
