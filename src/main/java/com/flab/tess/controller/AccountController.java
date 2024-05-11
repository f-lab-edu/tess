package com.flab.tess.controller;

import com.flab.tess.domain.Account;
import com.flab.tess.dto.AccountResponseDto;
import com.flab.tess.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
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

    //(1)
    @GetMapping
    public List<AccountResponseDto> getAccounts(){
        List<Account> accounts = accountService.getAccounts();
        return accounts.stream()
                .map(AccountResponseDto::from)
                .collect(Collectors.toList());
    }

    //(2)
    @GetMapping("/{accountId}")
    public AccountResponseDto getAccountOne(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        Account account = accountService.getAccountOne(accountId);
        return AccountResponseDto.from(account);
    }
}

