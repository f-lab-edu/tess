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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User join(User user){

        User encodeUser = user.encodePassword(passwordEncoder);
        user = userRepository.save(encodeUser);

        return user;
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));

        String password = loginRequest.getPassword();
        if (!user.checkPassword(passwordEncoder, password )) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return user;
    }

//    public User findUser(Principal principal){
//        return userRepository.findByLoginId(principal.getName()).get();
//    }

}
