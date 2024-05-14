package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
public class ReceiveAccountDto {

    private final BigInteger accountId;
    private final String accountNum;

    public static ReceiveAccountDto from(Account account){
        return ReceiveAccountDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .build();
    }

}
