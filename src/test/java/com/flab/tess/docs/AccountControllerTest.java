package com.flab.tess.docs;

import com.flab.tess.controller.AccountController;
import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.dto.AccountCreateRequest;
import com.flab.tess.dto.AccountResponse;
import com.flab.tess.dto.WithdrawalRequestDto;
import com.flab.tess.service.AccountService;
import com.flab.tess.service.CustomUserDetailService;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;


public class AccountControllerTest extends RestDocsTest {

    private final AccountService accountService = mock(AccountService.class);
    private final CustomUserDetailService customUserDetailService = mock(CustomUserDetailService.class);

    @Override
    protected Object initializeController() {
        return new AccountController(accountService, customUserDetailService);
    }

    @Test
    void 전체_계좌_조회() throws Exception{

        //given
        given(customUserDetailService.findUser(any(Principal.class))).willReturn(testUser);
        List<Account> testAccounts = testUser.getAccountList();
        given(accountService.getAccounts(testUser)).willReturn(testAccounts);

        //when & then
        this.mockMvc.perform(
                get("/accounts").principal(mockPrincipal))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
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
        Account account = testUser.getAccountList().get(1);

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

    @Test
    void 계좌_생성() throws Exception{

        //given
        AccountCreateRequest testAccountCreateRequest = new AccountCreateRequest("123", "test계좌", "입출금");
        Account account = Account.of(testUser, "123", "test계좌", "입출금");

        given(customUserDetailService.findUser(any(Principal.class))).willReturn(testUser);
        given(accountService.saveAccount(any(AccountCreateRequest.class), any(User.class))).willReturn(account);


        // When & Then
        mockMvc.perform(post("/accounts")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAccountCreateRequest)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                // 문서화
                .andDo(document("create-account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accountId").description("계좌 id"),
                                fieldWithPath("accountNum").description("계좌 번호"),
                                fieldWithPath("accountName").description("계좌 이름"),
                                fieldWithPath("accountType").description("계좌 유형"),
                                fieldWithPath("balance").description("잔액")
                        )));
    }

}
