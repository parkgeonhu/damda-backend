package com.sikugeon.damda.core.oauth.application;

import com.sikugeon.damda.core.oauth.domain.UserInfo;

public interface OAuthUserInfoService {
    UserInfo getUserInfo(String token);
}
