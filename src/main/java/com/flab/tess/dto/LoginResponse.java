package com.flab.tess.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private final String accessToken;
    private final BigInteger userId;

}