package com.sikugeon.damda.web.iam.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IamRegisterRequest {
    @NotNull
    private String username;
}
