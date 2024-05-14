package com.flab.tess.service;

import com.flab.tess.domain.User;
import com.flab.tess.domain.UserDetailImpl;
import com.flab.tess.dto.UserDto;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public User findUser(Principal principal){
        User user = userRepository.findByLoginId(principal.getName()).get();
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(UserDetailImpl::new)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
    }
}
