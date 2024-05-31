package com.flab.tess.dto;

import com.flab.tess.domain.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {

    private String name;
    private String loginId;
    private String password;

    @Builder
    public User toEntity(){
        return User.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .build();
    }
}
