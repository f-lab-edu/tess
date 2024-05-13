package com.flab.tess.dto;

import lombok.*;

import java.math.BigInteger;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private  String accessToken;
    private  BigInteger userId;

}