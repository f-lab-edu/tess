package com.flab.tess.dto;

import com.flab.tess.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
public class UserDto {

    private final BigInteger id;
    private final String userName;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getUserId())
                .userName(user.getName())
                .build();
    }
}
