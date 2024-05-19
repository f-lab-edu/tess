package com.flab.tess.service;

import com.flab.tess.domain.User;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User join(User user){

        User encodeUser = user.encodePassword();
        user = userRepository.save(encodeUser);

        return user;
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));

        String password = loginRequest.getPassword();
        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return user;
    }



}
