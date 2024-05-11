package com.flab.tess.dto;
import lombok.*;

@Getter
@Builder
@Setter
public class WithdrawalRequestDto {

    private String sendAccountId;
    private String receiveAccountNum;
    private String amount;

    public WithdrawalRequestDto() {
    }

    public  WithdrawalRequestDto(String sendAccountId, String receiveAccountNum, String amount) {
        this.sendAccountId = sendAccountId;
        this.receiveAccountNum = receiveAccountNum;
        this.amount = amount;
    }
}
