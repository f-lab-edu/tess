package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Builder
public class AccountAllDto {

    private BigInteger accountId;
    private String accountNum;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;


    public AccountAllDto from(Account account){
        return AccountAllDto.builder()
                .accountId(account.getAccountId())
                .accountNum(account.getAccountNum())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}
