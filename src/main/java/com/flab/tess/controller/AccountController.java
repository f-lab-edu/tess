package com.flab.tess.controller;

import com.flab.tess.dto.AccountDto;
import com.flab.tess.dto.EntityResponseDto;
import com.flab.tess.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * (1) 은행 계좌 목록 전체 조회 GET
 * (2) (1) 에서 전체 조회 할 때 전체 계좌의 잔액 조회 GET
 * (3) 개별 계좌별 거래 내역 상세 조회 GET
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor //생성자 자동주입
public class AccountController {

    private final AccountService accountService;

    //(1)
    @GetMapping("/all")
    public EntityResponseDto.getAccountAllResponseDto getAccountAll(){
        List<AccountDto> responseData = accountService.getAccountAll();
        return new EntityResponseDto.getAccountAllResponseDto(200,"모든 계좌 목록 조회 성공", responseData);
    }

//    //(2)
//    @GetMapping("/balance")
//    public EntityResponseDto.getAccountBalance getAccountAll(){
//        List<AccountDto> responseData = accountService.getAccountAll();
//        return new EntityResponseDto.getAccountAllResponseDto(200,"모든 계좌 목록 조회 성공", responseData);
//    }

    //(3)
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        AccountDto responseData = accountService.getAccountOne(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseData);
    }
}

