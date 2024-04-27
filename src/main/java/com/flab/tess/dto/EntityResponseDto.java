package com.flab.tess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EntityResponseDto {

    private int status;
    private String message;
    private Object data;

    @Getter
    @AllArgsConstructor
    public static class getAccountAllResponseDto{
        private int status;
        private String message;
        private List<AccountDto> data;
    }

    @Getter
    @AllArgsConstructor
    public static class getAccountOneResponseDto{
        private int status;
        private String message;
        private AccountDto data;
    }

    @Getter
    @AllArgsConstructor
    public static class postSend{
        private int status;
        private String message;
        private BigInteger transactionId;
    }

    @Getter
    @AllArgsConstructor
    public static class getTransactionOneResponseDto{
        private int status;
        private String message;
        private TransactionDto data;
    }

}
