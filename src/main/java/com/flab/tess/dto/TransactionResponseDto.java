package com.flab.tess.dto;

import com.flab.tess.domain.Transaction;
import com.flab.tess.util.CurrencyFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TransactionResponseDto {

    private final BigInteger transactionId;
    private final String amount;
    private final LocalDateTime transactionAt;
    private final AccountResponseDto sender;
    private final ReceiveAccountDto receiver;

    public static TransactionResponseDto from(Transaction transaction){
        return TransactionResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .amount(CurrencyFormatter.formatKRW(transaction.getAmount()))
                .transactionAt(transaction.getTransactionAt())
                .sender(AccountResponseDto.from(transaction.getSenderAccountId()))
                .receiver(ReceiveAccountDto.from(transaction.getReceiverAccountId()))
                .build();
    }
}
