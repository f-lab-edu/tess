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
//user(본인)의 계좌 정보
public class AccountResponse {

    private final BigInteger accountId;
    private final String accountNum;
    private final String accountName;
    private final String accountType;
    private final String balance;

    public static AccountResponse from(Account account){
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .balance(CurrencyFormatter.formatKRW(account.getBalance()))
                .build();
    }


}
