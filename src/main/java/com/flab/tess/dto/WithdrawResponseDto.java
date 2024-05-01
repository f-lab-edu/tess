package com.flab.tess.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
public class WithdrawResponseDto {
    private final BigInteger transactionId;
    private final BigDecimal amount;
}
