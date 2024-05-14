package com.flab.tess.docs;

import com.flab.tess.controller.AccountController;
import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.service.AccountService;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.restdocs.payload.JsonFieldType;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;


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
    private final CustomUserDetailService customUserDetailService = mock(CustomUserDetailService.class);


    @Override
    protected Object initializeController() {
        return new AccountController(accountService, customUserDetailService);
    }

    // Mock Principal
    static Principal mockPrincipal = new Principal() {
        @Override
        public String getName() {
            return "testUser";
        }
    };

    @Test
    void 전체_계좌_조회() throws Exception{

        //given
        User testUser = createUser();
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



    User createUser(){
        User testUser = mock(User.class);
        when(testUser .getUserId()).thenReturn(BigInteger.valueOf(1));
        when(testUser .getLoginId()).thenReturn("testLoginId");
        when(testUser .getName()).thenReturn("선재");

        Account testAccount = mock(Account.class);
        when(testAccount.getAccountId()).thenReturn(BigInteger.valueOf(11));
        when(testAccount.getAccountNum()).thenReturn("123456");
        when(testAccount.getAccountName()).thenReturn("선재 계좌");
        when(testAccount.getAccountType()).thenReturn("입출금");
        when(testAccount.getBalance()).thenReturn(BigDecimal.valueOf(10000));
        when(testAccount.getUser()).thenReturn(testUser );

        Account testAccount2 = mock(Account.class);
        when(testAccount2 .getAccountId()).thenReturn(BigInteger.valueOf(12));
        when(testAccount2 .getAccountNum()).thenReturn("77777");
        when(testAccount2 .getAccountName()).thenReturn("행운의 계좌");
        when(testAccount2 .getAccountType()).thenReturn("입출금");
        when(testAccount2 .getBalance()).thenReturn(BigDecimal.valueOf(777777));
        when(testAccount.getUser()).thenReturn(testUser );

        when(testUser.getAccountList()).thenReturn(List.of(testAccount, testAccount2));

        return testUser;
    }
}
