package com.flab.tess.controller;


import com.flab.tess.dto.EntityResponseDto;
import com.flab.tess.dto.SendDto;
import com.flab.tess.dto.TransactionDto;
import com.flab.tess.service.AccountService;
import com.flab.tess.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * (1) 송금 하기 POST
 * (2) 거래 내역 상세 조회 GET
 * (3) 송금 할 때 최근 보낸 계좌 조회 GET
 */
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
//    private final AccountService accountService;


    //(1) 송금 하기
    @PostMapping(value="/send", produces = "application/json; charset=UTF-8")
    public EntityResponseDto.postSend postSend(@RequestBody SendDto sendDto){
        BigInteger transactionId = transactionService.saveTransaction(sendDto);
        return new EntityResponseDto.postSend(200, "송금 성공", transactionId);
    }

    //(2) 거래 내역 상세 조회
    @GetMapping(value="/{transactionId}", produces = "application/json; charset=UTF-8")
    public EntityResponseDto.getTransactionOneResponseDto getTransactionOne(@PathVariable("transactionId") String id){
        BigInteger transactionId = new BigInteger(id);
        TransactionDto transactionDto =transactionService.getTransaction(transactionId);
        return new EntityResponseDto.getTransactionOneResponseDto(200,"거래 내역 상세 조회 성공", transactionDto);
    }

}
