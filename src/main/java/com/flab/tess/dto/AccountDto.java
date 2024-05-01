package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
public class AccountDto {

    private final BigInteger accountId;

    private final String accountNum;
    private final String accountName;
    private final String accountType;
    private final BigDecimal balance;

    public static AccountDto from(Account account){
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}
