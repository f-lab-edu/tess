package com.flab.tess.dto;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.util.CurrencyFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@Builder
@RequiredArgsConstructor
public class RecentWithdrawResponse {

    private final String userName;
    private final String accountNum;

    public static RecentWithdrawResponse from(Transaction transaction){
        return RecentWithdrawResponse.builder()
                .userName(transaction.getReceiverAccountId().getUser().getName())
                .accountNum(transaction.getReceiverAccountId().getAccountNum())
                .build();
    }


}
