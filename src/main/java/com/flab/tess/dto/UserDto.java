package com.flab.tess.dto;

import com.flab.tess.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class UserDto {

    private BigInteger userId;
    private String loginId;
    private String userName;

    public UserDto(BigInteger userId, String loginId, String userName) {
        this.userId = userId;
        this.loginId = loginId;
        this.userName = userName;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getUserId(), user.getLoginId(), user.getName());
    }

}
