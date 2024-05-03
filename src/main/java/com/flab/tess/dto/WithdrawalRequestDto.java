package com.flab.tess.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Builder(builderClassName = "DtoBuilder", toBuilder = true)
//JSON 객체를 자바 객체로 역직렬화 하기 위한 어노테이션, 역직렬화 과정에서 DtoBuilder 클래스 사용
@JsonDeserialize(builder=WithdrawalRequestDto.DtoBuilder.class)
@RequiredArgsConstructor
public class WithdrawalRequestDto {

    private final BigInteger sendAccountId;
    private final String receiveAccountNum;
    private final BigDecimal amount;

    //Jackson 라이브러리가 JSON을 인스턴스로 변환하는 과정에서 이 빌더를 사용함
    @JsonPOJOBuilder
    public static class DtoBuilder{}

}
