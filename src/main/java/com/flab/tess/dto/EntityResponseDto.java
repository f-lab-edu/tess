package com.flab.tess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
        private List<AccountAllDto> data;
    }

    @Getter
    @AllArgsConstructor
    public static class getAccountOneResponseDto{
        private int status;
        private String message;
        private AccountDto data;
    }

}
