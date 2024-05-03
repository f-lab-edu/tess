package com.flab.tess.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TransactionDto {

    private final BigInteger transactionId;
    private final BigDecimal amount;
    private final LocalDateTime transactionAt;
    private final AccountDto sender;
    private final ReceiveAccountDto receiver;

}
