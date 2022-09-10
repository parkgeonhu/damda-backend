package com.sikugeon.damda.core.oauth.application;

import com.sikugeon.damda.common.util.RandomUtils;
import com.sikugeon.damda.core.oauth.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenOAuthService implements OAuthService {

    GoogleOAuthUserInfoService googleOAuthUserInfoService;

    public TokenOAuthService(GoogleOAuthUserInfoService googleOAuthUserInfoService) {
        this.googleOAuthUserInfoService = googleOAuthUserInfoService;
    }

    @Override
    public String getId(Provider provider, String token) {
        UserInfo userInfo = null;
        String id = null;
        switch (provider) {
            case GOOGLE:
                userInfo = googleOAuthUserInfoService.getUserInfo(token);
                id = userInfo.getId();
                break;
            case TEST:
                id = RandomUtils.randomString();
                break;
        }
        return id;
    }
}
