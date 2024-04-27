package com.flab.tess.docs.account;

import com.flab.tess.controller.AccountController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.domain.AccountType;
import com.flab.tess.dto.AccountDto;
import com.flab.tess.service.AccountService;
import org.junit.jupiter.api.Test;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class AccountControllerTest extends RestDocsTest {

    private final AccountService accountService = mock(AccountService.class);

    @Override
    protected Object initializeController() {
        return new AccountController(accountService);
    }

    @Test
    void 개별_계좌_상세_조회() throws Exception{

        //given
        given(accountService.getAccountOne(any()))
                .willReturn(new AccountDto(
                        BigInteger.valueOf(10),
                        "123456",
                        "test",
                        BigDecimal.valueOf(10000)));

        //when & then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/account/{accountId}","10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                //문서화
                .andDo(document("get-account-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("accountId").description("계좌 id")),
                        responseFields(
                                fieldWithPath("accountId").description("계좌 id"),
                                fieldWithPath("accountNum").description("계좌 번호"),
                                fieldWithPath("accountName").description("계좌 이름"),
                                fieldWithPath("balance").description("잔액")
                        )));
    }


}
