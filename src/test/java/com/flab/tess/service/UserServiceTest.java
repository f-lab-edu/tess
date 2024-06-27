package com.flab.tess.service;

import com.flab.tess.domain.User;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 회원_가입() {
        User user = mock(User.class);
        User encodedUser = mock(User.class);
        when(user.encodePassword()).thenReturn(encodedUser);
        when(userRepository.save(encodedUser)).thenReturn(encodedUser);

        User result = userService.join(user);

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
    }

    @Test
    void 로그인() {
        String loginId = "testLoginId";
        String password = "testPassword";

        LoginRequest loginRequest = new LoginRequest(loginId, password);
        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(user.checkPassword(password)).thenReturn(true);

        User loginResult = userService.login(loginRequest);

        assertNotNull(loginResult);
        assertEquals(user, loginResult);
    }

    @Test
    void 잘못된_패스워드_로그인_예외() {

        String loginId = "testLoginId";
        String password = "invalidPassword";
        LoginRequest loginRequest = new LoginRequest(loginId, password);
        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(user.checkPassword(password)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @Test
    void 가입되지_않은_아이디_예외() {
        String loginId = "nonExistentUser";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(loginId, password);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("가입되지 않은 아이디 입니다.", exception.getMessage());
    }
}
