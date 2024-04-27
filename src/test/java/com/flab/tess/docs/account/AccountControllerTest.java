package com.flab.tess.docs.account;

import com.flab.tess.controller.AccountController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.dto.AccountDto;
import com.flab.tess.service.AccountService;
import org.junit.jupiter.api.Test;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    void 전체_계좌_조회() throws Exception{

        //given
        List<AccountDto> testAccounts = new ArrayList<>();
        testAccounts.add(new AccountDto(
                BigInteger.valueOf(1),
                "123456",
                "test1",
                "입출금",
                BigDecimal.valueOf(7777)
        ));
        testAccounts.add(new AccountDto(
                BigInteger.valueOf(2),
                "456789",
                "test2",
                "저축",
                BigDecimal.valueOf(100000)
        ));
        given(accountService.getAccountAll()).willReturn(testAccounts);

        //when & then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/account/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                //문서화
                .andDo(document("get-account-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터").type(JsonFieldType.ARRAY),
                                fieldWithPath("data[].accountId").description("계좌 id"),
                                fieldWithPath("data[].accountNum").description("계좌 번호"),
                                fieldWithPath("data[].accountName").description("계좌 이름"),
                                fieldWithPath("data[].accountType").description("계좌 타입"),
                                fieldWithPath("data[].balance").description("잔액").type(JsonFieldType.NUMBER)
                        )));
    }


    @Test
    void 개별_계좌_상세_조회() throws Exception{

        //given
        AccountDto testAccount = new AccountDto(BigInteger.valueOf(10),"123456","OneTest","입출금",BigDecimal.valueOf(10000));
        given(accountService.getAccountOne(BigInteger.valueOf(10))).willReturn(testAccount);

        //        given(accountService.getAccountOne(any()))
//                .willReturn(new AccountDto(
//                        BigInteger.valueOf(10),
//                        "123456",
//                        "test",
//                        "입출금",
//                        BigDecimal.valueOf(10000)));

        //when & then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/account/{accountId}","10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"))
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
                                fieldWithPath("accountType").description("계좌 타입"),
                                fieldWithPath("balance").description("잔액")
                        )));
    }


}
