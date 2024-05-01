package com.flab.tess.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

//생성자패턴 안 쓰고 빌더패턴만 쓰니까 테스트코드에서 Json 직렬화가 오류남..
@Getter
@Builder
public class WithdrawalRequestDto {

    private BigInteger sendAccountId;
    private String receiveAccountNum;
    private BigDecimal amount;

    public WithdrawalRequestDto(){};

    public WithdrawalRequestDto(BigInteger sendAccountId, String receiveAccountNum, BigDecimal amount) {
        this.sendAccountId = sendAccountId;
        this.receiveAccountNum = receiveAccountNum;
        this.amount = amount;
    }

}
