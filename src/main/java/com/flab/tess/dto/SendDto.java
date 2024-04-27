package com.flab.tess.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Builder

public class SendDto {

    private BigInteger sendAccountId;
    private String receiveAccountNum;
    private BigDecimal amount;

    public SendDto() {
    }

    public SendDto(BigInteger sendAccountId, String receiveAccountNum, BigDecimal amount) {
        this.sendAccountId = sendAccountId;
        this.receiveAccountNum = receiveAccountNum;
        this.amount = amount;
    }


}
