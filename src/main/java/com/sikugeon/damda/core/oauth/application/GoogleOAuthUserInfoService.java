package com.sikugeon.damda.core.oauth.application;

import com.sikugeon.damda.common.util.RestTemplateService;
import com.sikugeon.damda.core.oauth.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class GoogleOAuthUserInfoService implements OAuthUserInfoService {

    private final RestTemplateService restTemplateService;
    private final String BASE_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public UserInfo getUserInfo(String token) {
        Map<String, List<String>> headers = new HashMap<>();
        MultiValueMap<String, String> header = CollectionUtils.toMultiValueMap(headers);
        String id;

        try {
            ResponseEntity<HashMap> response = restTemplateService.get(BASE_URL + token, new HttpHeaders(header), HashMap.class);
            id = (String) response.getBody().get("email");
        } catch (Exception e) {
            id = null;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);

        return userInfo;
    }

}
