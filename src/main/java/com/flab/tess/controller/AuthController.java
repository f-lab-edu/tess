package com.flab.tess.controller;

import com.flab.tess.domain.User;
import com.flab.tess.dto.JoinRequest;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.dto.LoginResponse;
import com.flab.tess.dto.UserDto;
import com.flab.tess.provider.JwtTokenProvider;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/join")
    public UserDto join(@RequestBody JoinRequest joinRequest){
        User user = userService.join(joinRequest.toEntity());
        return UserDto.from(user);
    }

    @GetMapping("/info")
    public UserDto getUserInfo(Principal principal){
        // 캐시에서 UserDto 가져오기 또는 캐시 미스 시 캐시에 저장 및 반환
        return customUserDetailService.getCachedUserDto(principal);
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);
        String accessToken = jwtTokenProvider.createToken(user.getLoginId());
        return new LoginResponse(accessToken, user.getUserId());
    }


}
