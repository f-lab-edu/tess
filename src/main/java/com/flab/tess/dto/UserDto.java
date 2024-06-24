package com.flab.tess.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@RequiredArgsConstructor
public class UserDto {

    private final BigInteger userId;
    private final String loginId;
    private final String userName;

}
