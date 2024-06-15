package com.flab.tess.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;

public class UserTest {

    private User user;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(BigInteger.valueOf(1))
                .loginId("testLoginId")
                .name("테스트 사용자")
                .password("plainPassword")
                .build();
    }

    @Test
    void encodePasswordTest() {
        user.encodePassword();

        assertNotNull(user.getPassword());
        assertTrue(passwordEncoder.matches("plainPassword", user.getPassword()), "암호 인코딩 확인");
    }

    @Test
    void checkPasswordTest() {
        user.encodePassword();

        assertTrue(user.checkPassword("plainPassword"), "암호가 알맞게 체크되는지 확인합니다.");
        assertFalse(user.checkPassword("wrongPassword"), "잘못된 암호가 체크되는지 확인합니다.");
    }


}
