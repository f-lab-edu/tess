package com.flab.tess.controller;


import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.*;
import com.flab.tess.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (1) 송금 하기 POST
 * (2) 거래 내역 상세 조회 GET
 * (3) 거래 내역 계좌별 전체 조회 GET
 * (4) 송금 할 때 최근 보낸 계좌 조회 GET
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //(1) 송금 하기
    @PostMapping("/withdraw")
    public WithdrawResponseDto withDraw(@RequestBody WithdrawalRequestDto withdrawalRequestDto){
        Transaction transaction = transactionService.saveTransaction(withdrawalRequestDto);
        return WithdrawResponseDto.from(transaction);
    }

    //(2) 거래 내역 상세 조회
    @GetMapping("/{transactionId}")
    public TransactionResponseDto getTransactionOne(@PathVariable("transactionId") String id){
        BigInteger transactionId = new BigInteger(id);
        Transaction transaction = transactionService.getTransaction(transactionId);
        return TransactionResponseDto.from(transaction);
    }

    //(3) 거래 내역 계좌별 전체 조회 GET
    @GetMapping("/accounts/{accountId}")
    public List<TransactionResponseDto> getTransactionAll(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        List<Transaction> transactions = transactionService.getTransactionAll(accountId);
        return transactions.stream() // 리스트를 스트림으로 변환
                .map(TransactionResponseDto::from)
                .collect(Collectors.toList());
    }

//    //(4) 송금 할 때 최근 보낸 계좌 조회
//    @GetMapping(value = "/{accountId}")
//    public ResponseEntity<List<RecentWithdrawDto>> getRecentWithdraw(@PathVariable("accountId") String id){
//        BigInteger accountId = new BigInteger(id);
//        List<RecentWithdrawDto> recentWithdrawDto = transactionService.
//        return ResponseEntity.status(HttpStatus.OK).body(recentWithdrawDto);
//    }


}
