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
public class TransactionAllResponse {

    private final BigInteger transactionId;
    private final TransactionType transactionType;

    private final String amount;
    private final LocalDateTime transactionAt;
    private final String balance;
    private final String otherAccountName;

    public static TransactionAllResponse from(Transaction transaction, TransactionType transactionType, BigDecimal balance, Account account){
        return TransactionAllResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transactionType)
                .amount(CurrencyFormatter.formatKRW(transaction.getAmount()))
                .transactionAt(transaction.getTransactionAt())
                .balance(CurrencyFormatter.formatKRW(balance))
                .otherAccountName(account.getAccountName())
                .build();
    }
}
