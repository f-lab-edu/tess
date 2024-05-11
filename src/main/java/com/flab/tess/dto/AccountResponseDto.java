package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import com.flab.tess.util.CurrencyFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigInteger;


@Getter
@Builder
@RequiredArgsConstructor
public class AccountResponseDto{

    private final BigInteger accountId;
    private final String accountNum;
    private final String accountName;
    private final String accountType;
    private final String balance;

    public static AccountResponseDto from(Account account){
        return AccountResponseDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .balance(CurrencyFormatter.formatKRW(account.getBalance()))
                .build();
    }


}
