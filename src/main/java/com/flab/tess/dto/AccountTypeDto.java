//package com.flab.tess.dto;
//
//import com.flab.tess.domain.AccountType;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//import java.math.BigInteger;
//
//@Getter
//@Setter
//@Builder
//@RequiredArgsConstructor
//public class AccountTypeDto {
//    private final BigInteger accountTypeId;
//    private final String accountTypeName;
//
//    public static AccountTypeDto from(AccountType accountType){
//        return AccountTypeDto.builder()
//                .accountTypeId(accountType.getAccountTypeId())
//                .accountTypeName(accountType.getAccountTypeName())
//                .build();
//    }
//}
