package com.flab.tess.docs.account;

import com.flab.tess.controller.AccountController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.domain.Account;
import com.flab.tess.dto.AccountResponseDto;
import com.flab.tess.service.AccountService;
import org.junit.jupiter.api.Test;

import org.springframework.restdocs.payload.JsonFieldType;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;


public class AccountControllerTest extends RestDocsTest {

    private final AccountService accountService = mock(AccountService.class);

    @Override
    protected Object initializeController() {
        return new AccountController(accountService);
    }

    @Test
    void 전체_계좌_조회() throws Exception{

        //given
        Account account1 = mock(Account.class);
        Account account2 = mock(Account.class);

        when(account1.getAccountId()).thenReturn(BigInteger.valueOf(11));
        when(account1.getAccountNum()).thenReturn("123456");
        when(account1.getAccountName()).thenReturn("선재 계좌");
        when(account1.getAccountType()).thenReturn("입출금");
        when(account1.getBalance()).thenReturn(BigDecimal.valueOf(10000));

        when(account2.getAccountId()).thenReturn(BigInteger.valueOf(12));
        when(account2.getAccountNum()).thenReturn("77777");
        when(account2.getAccountName()).thenReturn("행운의 계좌");
        when(account2.getAccountType()).thenReturn("입출금");
        when(account2.getBalance()).thenReturn(BigDecimal.valueOf(777777));

        given(accountService.getAccounts()).willReturn(Arrays.asList(account1, account2));

        //when & then
        this.mockMvc.perform(
                get("/accounts"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                //문서화
                .andDo(document("get-accounts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].accountId").description("계좌 id").type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("[].accountNum").description("계좌 번호").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].accountName").description("계좌 이름").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].accountType").description("계좌 타입").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].balance").description("잔액").type(JsonFieldType.STRING).optional()
                        )));
    }


    @Test
    void 개별_계좌_상세_조회() throws Exception{

        //given
        Account account = mock(Account.class);
        when(account.getAccountId()).thenReturn(BigInteger.valueOf(12));
        when(account.getAccountNum()).thenReturn("77777");
        when(account.getAccountName()).thenReturn("행운의 계좌");
        when(account.getAccountType()).thenReturn("입출금");
        when(account.getBalance()).thenReturn(BigDecimal.valueOf(777777));

        given(accountService.getAccountOne(BigInteger.valueOf(12))).willReturn(account);

        //when & then
        this.mockMvc.perform(
                get("/accounts/{accountId}","12"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(status().isOk())
                //문서화
                .andDo(document("get-accounts-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("accountId").description("계좌 id")),
                        responseFields(
                                fieldWithPath("accountId").description("계좌 id").type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("accountNum").description("계좌 번호").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("accountName").description("계좌 이름").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("accountType").description("계좌 타입").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("balance").description("잔액").type(JsonFieldType.STRING).optional()
                        )));
    }


}
