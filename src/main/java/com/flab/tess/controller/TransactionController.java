package com.flab.tess.controller;


import com.flab.tess.domain.TransactionType;
import com.flab.tess.domain.Transaction;
import com.flab.tess.domain.User;
import com.flab.tess.dto.*;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    private final CustomUserDetailService customUserDetailService;


    //(1) 송금 하기
    @PostMapping("/withdraw")
    public WithdrawResponseDto withDraw(@RequestBody WithdrawalRequestDto withdrawalRequestDto){
        Transaction transaction = transactionService.saveTransaction(withdrawalRequestDto);
        return WithdrawResponseDto.from(transaction);
    }

    //(2) 거래 내역 상세 조회
    @GetMapping("/{transactionId}")
    public TransactionResponse getTransactionOne(Principal principal, @PathVariable("transactionId") String id){
        User user = customUserDetailService.findUser(principal);
        BigInteger transactionId = new BigInteger(id);
        Transaction transaction = transactionService.getTransaction(transactionId);
        //+ 내역인지(user에게 입금된 내역=> user가 receiver인 거래 내역)
        if(transaction.getReceiverAccountId().getUser().equals(user)){
            return TransactionResponse.from(transaction, transaction.getReceiverAccountId(), transaction.getReceiverBalance(), TransactionType.WITHDRAW, transaction.getSenderAccountId());
        }
        //- 내역인지(user로부터 출금된 내역=> user가 sender인 거래 내역)
        else{
            return TransactionResponse.from(transaction, transaction.getSenderAccountId(), transaction.getSenderBalance(), TransactionType.DEPOSIT, transaction.getReceiverAccountId());
        }
    }

    //(3) 거래 내역 계좌별 전체 조회 GET
    @GetMapping("/accounts/{accountId}")
    public List<TransactionAllResponse> getTransactionAll(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);

        List<TransactionAllResponse> transactionAllResponses = new ArrayList<>();
        //+ 내역인지(user에게 입금된 내역=> user가 receiver인 거래 내역)
        List<Transaction> transactionsWithdraw = transactionService.getTransactionWithdraw(accountId);
        //- 내역인지(user로부터 출금된 내역=> user가 sender인 거래 내역)
        List<Transaction> transactionsDeposit = transactionService.getTransactionDeposit(accountId);

        // Withdraw transactions
        transactionAllResponses.addAll(
                transactionsWithdraw.stream()
                        .map(transaction -> { return TransactionAllResponse.from(transaction, TransactionType.WITHDRAW, transaction.getReceiverBalance(), transaction.getSenderAccountId());
                        })
                        .collect(Collectors.toList())
        );

        // Deposit transactions
        transactionAllResponses.addAll(
                transactionsDeposit.stream()
                        .map(transaction -> { return TransactionAllResponse.from(transaction, TransactionType.DEPOSIT, transaction.getSenderBalance(), transaction.getReceiverAccountId());
                        })
                        .collect(Collectors.toList())
        );

        // Sort by transactionAt
        return transactionAllResponses.stream()
                .sorted(Comparator.comparing(TransactionAllResponse::getTransactionAt))
                .collect(Collectors.toList());
    }

    //(4) user가 최근 송금한 계좌 조회
    @GetMapping(value = "withdraw/{accountId}")
    public List<RecentWithdrawResponse> getRecentWithdraw(@PathVariable("accountId") String id){
        BigInteger accountId = new BigInteger(id);
        // user가 sender인 내역
        List<Transaction> transactions = transactionService.getTransactionDeposit(accountId);

        // 중복된 accountNum을 제외하고 최대 5개의 결과 반환
        Set<String> seenAccountNum = new java.util.HashSet<>();
        return transactions.stream()
                .filter(transaction -> seenAccountNum.add(transaction.getReceiverAccountId().getAccountNum()))
                .limit(5)
                .map(RecentWithdrawResponse::from)
                .collect(Collectors.toList());
    }


}
