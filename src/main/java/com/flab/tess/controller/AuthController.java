package com.flab.tess.controller;

import com.flab.tess.dto.JoinRequest;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.dto.LoginResponse;
import com.flab.tess.dto.UserDto;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/join")
    public UserDto join(@RequestBody JoinRequest joinRequest){
        return userService.join(joinRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }



}
