package com.sikugeon.damda.web.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequest {
    String provider;

    @JsonProperty("access_token")
    String accessToken;
}
