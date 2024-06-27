package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.dto.AccountCreateRequest;
import com.flab.tess.dto.AccountResponse;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@EnableCaching
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Configuration
    static class Config {
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("accountCache");
        }

        @Bean
        public AccountService accountService(AccountRepository accountRepository) {
            return new AccountService(accountRepository);
        }
    }

    @Test
    void 계좌_상세_조회() {
        BigInteger accountId = BigInteger.valueOf(1);

        Account account = mock(Account.class);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountOne(accountId);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void 계좌_없음_예외() {
        BigInteger accountId = BigInteger.valueOf(1);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Account result = accountService.getAccountOne(accountId);

        assertNull(result);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void 계좌_전체_조회(){
        String loginId = "testLoginId";
        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(user.getUserId()).thenReturn(BigInteger.ONE);
        when(user.getLoginId()).thenReturn("testLoginId");
        when(user.getName()).thenReturn("Test User");

        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findByUser(user)).thenReturn(accounts);

        List<Account> result = accountService.getAccounts(user);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void 계좌_전체_조회_캐싱() {
        String loginId = "testLoginId";
        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(user.getUserId()).thenReturn(BigInteger.ONE);
        when(user.getLoginId()).thenReturn("testLoginId");
        when(user.getName()).thenReturn("Test User");

        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findByUser(user)).thenReturn(accounts);

        // 첫 번째 호출
        List<AccountResponse> result = accountService.getCachedAccountResponses(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findByUser(user);

        // 두 번째 호출(캐시에서 가져옴, 캐시 저장될 때의 초기 쿼리 1번만 있어야 함 새롭게 쿼리 추가 X)
        result = accountService.getCachedAccountResponses(user);
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findByUser(user); // 호출 횟수 여전히 1회
    }

    @Test
    void 계좌_개설_저장(){

        //given
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest("1234", "testAccount", "입출금");
        User user = mock(User.class);
        Account account = Account.of(user, accountCreateRequest.getAccountNum(), accountCreateRequest.getAccountName(), accountCreateRequest.getAccountType());

        //stub
        given(accountRepository.save(account)).willReturn(account);

        //when
        Account resultAccount = accountService.saveAccount(accountCreateRequest, user);

        //then
        assertEquals(account.getAccountId(), resultAccount.getAccountId());
        assertNotNull(resultAccount);

    }

}


