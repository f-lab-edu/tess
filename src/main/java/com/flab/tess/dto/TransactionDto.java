package com.flab.tess.dto;

import com.flab.tess.domain.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class TransactionDto {

    private final BigInteger transactionId;
    private final BigDecimal amount;
    private final Timestamp transactionAt;
    private final AccountDto sender;
    private final ReceiveAccountDto receiver;

//    public static TransactionDto from(Transaction transaction){
//        return TransactionDto.builder()
//                .transactionId(transaction.getTransactionId())
//                .amount(transaction.getAmount())
//                .transactionAt(transaction.getTransactionAt())
//                .build();
//    }
}
