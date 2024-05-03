package com.flab.tess.controller;


import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.RecentWithdrawDto;
import com.flab.tess.dto.WithdrawResponseDto;
import com.flab.tess.dto.WithdrawalRequestDto;
import com.flab.tess.dto.TransactionDto;
import com.flab.tess.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * (1) 송금 하기 POST
 * (2) 거래 내역 상세 조회 GET
 * (3) 거래 내역 전체 조회 GET
 * (4) 송금 할 때 최근 보낸 계좌 조회 GET
 */
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
//    private final AccountService accountService;

    //(1) 송금 하기
    @PostMapping(value="/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WithdrawResponseDto> withDraw(@RequestBody WithdrawalRequestDto withdrawalRequestDto){
        WithdrawResponseDto withdrawResponseDto = transactionService.saveTransaction(withdrawalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(withdrawResponseDto);
    }

    //(2) 거래 내역 상세 조회
    @GetMapping(value="/{transactionId}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<TransactionDto> getTransactionOne(@PathVariable("transactionId") String id){
        BigInteger transactionId = new BigInteger(id);
        TransactionDto transactionDto =transactionService.getTransaction(transactionId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionDto);
    }

    @GetMapping(value="/accounts/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionAll(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        List<TransactionDto> transactionDtos = transactionService.getTransactionAll(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionDtos);
    }

//    //(4) 송금 할 때 최근 보낸 계좌 조회
//    @GetMapping(value = "/{accountId}")
//    public ResponseEntity<List<RecentWithdrawDto>> getRecentWithdraw(@PathVariable("accountId") String id){
//        BigInteger accountId = new BigInteger(id);
//        List<RecentWithdrawDto> recentWithdrawDto = transactionService.
//        return ResponseEntity.status(HttpStatus.OK).body(recentWithdrawDto);
//    }

}
