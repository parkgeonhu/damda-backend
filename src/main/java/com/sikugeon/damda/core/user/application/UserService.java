package com.sikugeon.damda.core.user.application;

import com.sikugeon.damda.common.util.RandomUtils;
import com.sikugeon.damda.core.aws.iam.application.IamEditor;
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
        iamEditor.createIAMUser(username);
        Map map = iamEditor.createIAMAccessKey(username);
        iamEditor.addUserToGroup(username, "damda");

        user.updateAWSKey((String) map.get("accessKeyId"), (String) map.get("accessKeyId"));
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