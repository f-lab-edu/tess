package com.flab.tess.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@Builder
@RequiredArgsConstructor
public class RecentWithdrawDto {

    private final String userNames;
    private final String accountNums;

}
