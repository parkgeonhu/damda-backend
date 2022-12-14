package com.sikugeon.damda.web.auth;

import com.sikugeon.damda.core.aws.iam.application.IamEditor;
import com.sikugeon.damda.core.oauth.application.OAuthService;
import com.sikugeon.damda.core.oauth.application.Provider;
import com.sikugeon.damda.core.user.application.UserService;
import com.sikugeon.damda.core.user.domain.User;
import com.sikugeon.damda.jwt.JwtProperties;
import com.sikugeon.damda.jwt.JwtUtils;
import com.sikugeon.damda.web.auth.dto.OAuthLoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthRestController {

    OAuthService oAuthService;
    UserService userService;
    IamEditor iamEditor;

    @Value("${secretKey}")
    private String SECRET_KEY;


    public AuthRestController(OAuthService oAuthService,
                              UserService userService) {
        this.oAuthService = oAuthService;
        this.userService = userService;
    }

    @PostMapping("/api/auth/token/login")
    public ResponseEntity<Map> tokenLogin(@RequestBody OAuthLoginRequest oAuthLoginRequest, HttpServletResponse response){
        String id = oAuthService.getId(Provider.valueOf(oAuthLoginRequest.getProvider()), oAuthLoginRequest.getAccessToken());
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Unauthorized"));
        }

        User user = userService.findByUsername(id);
        if (user == null) { // 회원가입이 안되어있을 시
            user = userService.signup(id, SECRET_KEY);
        }

        String jwtToken = JwtUtils.createToken(user);

        Map<String, String> body = new HashMap();
        body.put("id", id);
        body.put(JwtProperties.COOKIE_NAME, jwtToken);

        //쿠키 생성
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, jwtToken);
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
    }

}
