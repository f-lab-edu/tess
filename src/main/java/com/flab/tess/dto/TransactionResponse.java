package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.TransactionType;
import com.flab.tess.domain.Transaction;
import com.flab.tess.util.CurrencyFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TransactionResponse {

    private final BigInteger transactionId;
    private final String amount;
    private final LocalDateTime transactionAt;
    private final TransactionType transactionType;

    private final UserInfo userInfo;

    //상대방의 계좌이름
    private final String otherAccountName;

    @Getter
    @RequiredArgsConstructor
    public static class UserInfo {
        private final String accountName;
        private final String accountNum;
        private final String balance;
    }

    public static TransactionResponse from(Transaction transaction, Account userAccount, BigDecimal balance, TransactionType transactionType, Account otherAccount){
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .amount(CurrencyFormatter.formatKRW(transaction.getAmount()))
                .transactionAt(transaction.getTransactionAt())
                .transactionType(transactionType)
                .userInfo(new UserInfo(userAccount.getAccountName(), userAccount.getAccountNum(), CurrencyFormatter.formatKRW(balance)))
                .otherAccountName(otherAccount.getAccountName())
                .build();
    }
}
