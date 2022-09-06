package com.sikugeon.damda.web.auth;

import com.sikugeon.damda.core.oauth.application.OAuthService;
import com.sikugeon.damda.core.oauth.application.Provider;
import com.sikugeon.damda.web.auth.dto.VerifyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthRestController {

    OAuthService oAuthService;

    public AuthRestController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping("/api/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest verifyRequest) {
        String id = oAuthService.getId(Provider.valueOf(verifyRequest.getProvider()), verifyRequest.getAccessToken());
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(id);
    }

}
