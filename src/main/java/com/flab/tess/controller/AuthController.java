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
        User user = userService.join(joinRequest);
        return convertToUserDto(user);
    }

    @GetMapping("/info")
    public UserDto getUserInfo(Principal principal){
        if (principal == null) {
            log.error("Principal is null");
            throw new IllegalArgumentException("Principal cannot be null");
        }
        User user = customUserDetailService.findUser(principal);
        return convertToUserDto(user);
    }

    private UserDto convertToUserDto(User user){
        return new UserDto(user.getUserId(), user.getLoginId(), user.getName());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);
        String accessToken = jwtTokenProvider.createToken(user.getLoginId());
        return new LoginResponse(accessToken, user.getUserId());
    }


}
