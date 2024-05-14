package com.flab.tess.dto;

import com.flab.tess.domain.Transaction;
import com.flab.tess.util.CurrencyFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
public class WithdrawResponseDto {
    private final BigInteger transactionId;
    private final String amount;

    public static WithdrawResponseDto from(Transaction transaction){
        return WithdrawResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .amount(CurrencyFormatter.formatKRW(transaction.getAmount()))
                .build();
    }

}
