package com.flab.tess.domain;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AccountTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(BigInteger.valueOf(1))
                .loginId("testLoginId")
                .name("선재")
                .build();
    }

    @Test
    void 계좌생성시_초기잔액_0() {
        Account account = Account.builder()
                .user(user)
                .accountNum("123456")
                .accountName("테스트 계좌")
                .accountType("입출금")
                .build();

        account.createBalance();

        assertEquals(BigDecimal.ZERO, account.getBalance(), "초기 잔액은 0이어야 합니다.");
    }

    @Test
    void 계좌로_입금후_잔액_업데이트() {
        Account account = Account.builder()
                .user(user)
                .accountNum("123456")
                .accountName("테스트 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(1000))
                .build();

        account.deposit(BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(1500), account.getBalance(), "해당 계좌로 500 입금 후 잔액은 1500이어야 합니다.");
    }

    @Test
    void 계좌에서_출금후_잔액_업데이트() {
        Account account = Account.builder()
                .user(user)
                .accountNum("123456")
                .accountName("테스트 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(1000))
                .build();

        account.withdraw(BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), account.getBalance(), "해당 계좌에서 500 출금 후 잔액은 500이어야 합니다.");
    }

    @Test
    void 출금시_잔액확인() {
        Account account = Account.builder()
                .user(user)
                .accountNum("123456")
                .accountName("테스트 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(1000))
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.valueOf(1500));
        });

        assertEquals("잔액이 부족합니다.", exception.getMessage());
    }

    @Test
    void 계좌_팩토리메서드_객체_생성_확인() {
        Account account = Account.of(user, "123456", "테스트 계좌", "입출금");

        assertNotNull(account);
        assertEquals(user, account.getUser());
        assertEquals("123456", account.getAccountNum());
        assertEquals("테스트 계좌", account.getAccountName());
        assertEquals("입출금", account.getAccountType());
    }
}
