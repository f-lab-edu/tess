package com.flab.tess.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    //Json 데이터 객체로 변환
    @JsonCreator
    public static AccountResponse create(@JsonProperty("accountId") BigInteger accountId,
                                         @JsonProperty("accountNum") String accountNum,
                                         @JsonProperty("accountName") String accountName,
                                         @JsonProperty("accountType") String accountType,
                                         @JsonProperty("balance") String balance) {
        return new AccountResponse(accountId, accountNum, accountName, accountType, balance);
    }

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
