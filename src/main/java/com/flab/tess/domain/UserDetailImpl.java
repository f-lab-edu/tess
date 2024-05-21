package com.flab.tess.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class UserDetailImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired(); // 계정이 만료되지 않았는지 반환
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked(); // 계정이 잠겨 있지 않은지 반환
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired(); // 자격 증명이 만료되지 않았는지 반환
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // 계정이 활성화되어 있는지 반환
    }
}

