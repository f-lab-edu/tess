package com.flab.tess.controller;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.dto.AccountResponse;
import com.flab.tess.service.AccountService;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (1) 은행 계좌 목록 전체 조회 GET
 * (2) 개별 계좌별 거래 내역 상세 조회 GET
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor //생성자 자동주입
public class AccountController {

    private final AccountService accountService;
//    private final UserService userService;
    private final CustomUserDetailService customUserDetailService;

    //(1)
    @GetMapping
    public List<AccountResponse> getAccounts(Principal principal){
        User user = customUserDetailService.findUser(principal);
        List<Account> accounts = accountService.getAccounts(user);
        return accounts.stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
    }

    //(2)
    @GetMapping("/{accountId}")
    public AccountResponse getAccountOne(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        Account account = accountService.getAccountOne(accountId);
        return AccountResponse.from(account);
    }
}

