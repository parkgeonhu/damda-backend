package com.sikugeon.damda.core.user.application;

import com.sikugeon.damda.common.util.RandomUtils;
import com.sikugeon.damda.core.aws.iam.application.IamEditor;
import com.sikugeon.damda.core.aws.s3.application.S3Manager;
import com.sikugeon.damda.core.user.domain.User;
import com.sikugeon.damda.core.user.exception.AlreadyRegisteredUserException;
import com.sikugeon.damda.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IamEditor iamEditor;
    private final S3Manager s3Manager;

    /**
     * 유저 등록
     *
     * @param username username
     * @param password password
     * @return 유저 권한을 가지고 있는 유저
     */
    public User signup(
            String username,
            String password
    ) {
        if (userRepository.findByUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        User user = new User(username, passwordEncoder.encode(password), "ROLE_USER");
        user.updateBucketName(RandomUtils.randomString());

        //IAM 설정
        iamEditor.createIAMUser(username);
        Map<String, String> awsKey = iamEditor.createIAMAccessKey(username);
        iamEditor.addUserToGroup(username, "damda");
        try {
            Thread.sleep(15000);
        } catch (Exception e) {

        }

        //S3 설정
        s3Manager.createBucket(awsKey, user.getBucketName());
        s3Manager.addPolicyToBucket(awsKey, user.getBucketName());

        user.updateAWSKey(awsKey);
        return userRepository.save(user);
    }

    /**
     * 관리자 등록
     *
     * @param username username
     * @param password password
     * @return 관리자 권한을 가지고 있는 유저
     */
    public User signupAdmin(
            String username,
            String password
    ) {
        if (userRepository.findByUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}