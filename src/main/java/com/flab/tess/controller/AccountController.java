package com.flab.tess.controller;

import com.flab.tess.dto.AccountDto;
import com.flab.tess.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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
    @GetMapping()
    public ResponseEntity<List<AccountDto>> getAccounts(){
        List<AccountDto> responseData = accountService.getAccountAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseData);
    }

    //(2)
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountOne(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        AccountDto responseData = accountService.getAccountOne(accountId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseData);
    }
}

