package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class AccountDto {

    private final BigInteger accountId;

    private final String accountNum;
    private final String accountName;
//    private final AccountType accountType;
    private final BigDecimal balance;


    public static AccountDto from(Account account){
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .accountName(account.getAccountName())
//                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}
