package com.flab.tess.service;

import com.flab.tess.domain.User;
import com.flab.tess.domain.UserDetailImpl;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Cacheable(value = "userCache", key = "#principal.name")
    public User findUser(Principal principal){
        log.info("cache miss " + principal.getName());
        return userRepository.findByLoginId(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(UserDetailImpl::new)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
    }
}
