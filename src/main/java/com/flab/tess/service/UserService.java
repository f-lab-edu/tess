package com.flab.tess.service;

import com.flab.tess.domain.User;
import com.flab.tess.dto.JoinRequest;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.dto.LoginResponse;
import com.flab.tess.dto.UserDto;
import com.flab.tess.provider.JwtTokenProvider;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Join;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto join(JoinRequest joinRequest){

        User user = joinRequest.toEntity();
        User encodeUser = user.encodePassword(passwordEncoder);
        user = userRepository.save(encodeUser);

        return UserDto.from(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));

        String password = loginRequest.getPassword();
        if (!user.checkPassword(passwordEncoder, password )) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtTokenProvider.createToken(user.getLoginId());
        BigInteger userId = user.getUserId();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(userId);
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }

}
