package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@Builder
@RequiredArgsConstructor
//거래를 하는 상대방의 계좌 정보니까 계좌번호만 노출
public class OtherAccountDto {

    private final BigInteger accountId;
    private final String accountNum;

    public static OtherAccountDto from(Account account){
        return OtherAccountDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .build();
    }

}
