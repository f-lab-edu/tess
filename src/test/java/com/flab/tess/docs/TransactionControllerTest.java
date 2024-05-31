package com.flab.tess.docs;

import com.flab.tess.controller.TransactionController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.domain.User;
import com.flab.tess.dto.*;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.TransactionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

public class TransactionControllerTest extends RestDocsTest {

    private final TransactionService transactionService = mock(TransactionService.class);
    private final CustomUserDetailService customUserDetailService = mock(CustomUserDetailService.class);

    @Override
    protected Object initializeController() {
        return new TransactionController(transactionService, customUserDetailService);
    }


    @Test
    void 계좌별_전체_거래_내역_조회() throws Exception{

        List<Transaction> mockTransactions = createTransactions();

        given(transactionService.getTransactionDeposit(BigInteger.valueOf(11))).willReturn(Collections.singletonList(mockTransactions.get(0)));
        given(transactionService.getTransactionWithdraw(BigInteger.valueOf(11))).willReturn(Collections.singletonList(mockTransactions.get(1)));

        //when&then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                .get("/transactions/accounts/{accountId}",11))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("get-transaction-account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("accountId").description("계좌 id")),
                        responseFields(
                                fieldWithPath("[].transactionId").description("거래 내역 id").type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("[].transactionType").description("거래 타입").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].amount").description("거래 금액").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].transactionAt").description("거래 시간").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("[].balance").description("거래 후 계좌 잔액").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].otherAccountName").description("거래 상대 계좌명").type(JsonFieldType.STRING).optional()
                        )));

    }

    @Test
    void 거래_내역_상세_조회() throws Exception{

        //given
        given(customUserDetailService.findUser(any(Principal.class))).willReturn(testUser);
        Transaction mockTransaction = createTransactions().get(0);
        given(transactionService.getTransaction(BigInteger.valueOf(1))).willReturn(mockTransaction);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/transactions/{transactionId}",1)
                .principal(mockPrincipal))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //문서화
                .andDo(document("get-transaction-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("transactionId").description("거래 내역 ID")),
                        responseFields(
                                fieldWithPath("transactionId").description("거래 내역 ID").type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("amount").description("거래 금액").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("transactionAt").description("거래 일시").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("transactionType").description("거래 타입").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("userInfo.accountName").description("user 계좌명").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("userInfo.accountNum").description("user 계좌 번호").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("userInfo.balance").description("거래 후 계좌 잔액").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("otherAccountName").description("거래 상대 계좌명").type(JsonFieldType.STRING).optional()
                        )));
    }


    @Test
    void 송금_하기() throws Exception{

        //given
        WithdrawalRequestDto testRequestDto = WithdrawalRequestDto.builder()
                .sendAccountId("1")
                .receiveAccountNum("123456")
                .amount("2000")
                .build();

        //given
        Transaction mockTransaction = mock(Transaction.class);
        when(mockTransaction.getTransactionId()).thenReturn(BigInteger.valueOf(1));
        when(mockTransaction.getAmount()).thenReturn(BigDecimal.valueOf(2000));  // 예시 금액
        when(mockTransaction.getTransactionAt()).thenReturn(LocalDateTime.now());  // 예시 거래 일시

        given(transactionService.saveTransaction(any(WithdrawalRequestDto.class))).willReturn(mockTransaction);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/transactions/withdraw")
                .content( objectMapper.writeValueAsString(testRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                 //문서화
                .andDo(document("withdraw-transaction",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("transactionId").description("거래내역 ID").type(JsonFieldType.NUMBER).optional(),
                        fieldWithPath("amount").description("거래금액").type(JsonFieldType.STRING).optional()
                )));

    }

    @Test
    void 최근_송금_내역_조회() throws Exception{

        List<Transaction> mockTransactions = createTransactions();

        given(transactionService.getTransactionDeposit(BigInteger.valueOf(11))).willReturn(Collections.singletonList(mockTransactions.get(0)));

        //when&then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/transactions/withdraw/{accountId}",11))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("get-recent-withdraw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("accountId").description("계좌 id")),
                        responseFields(
                                fieldWithPath("[].userName").description("최근 송금 보낸 유저 이름").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("[].accountNum").description("최근 송금 보낸 계좌 번호").type(JsonFieldType.STRING).optional()
                        )));

    }


    List<Transaction> createTransactions(){

        Account userAccount = testUser.getAccountList().get(0);

        User otherUser = User.builder()
                .userId(BigInteger.valueOf(2))
                .loginId("seonjae")
                .name("선재")
                .build();

        Account otherAccount = Account.builder()
                .accountId(BigInteger.valueOf(3))
                .accountNum("123")
                .accountName("상대방 계좌")
                .balance(BigDecimal.valueOf(12000))
                .user(otherUser )
                .build();

        Transaction depositTransaction = Transaction.builder()
                .transactionId(BigInteger.valueOf(1))
                .amount(BigDecimal.valueOf(5000))
                .transactionAt(LocalDateTime.now())
                .senderAccountId(userAccount)
                .receiverAccountId(otherAccount)
                .senderBalance(userAccount.getBalance().subtract(BigDecimal.valueOf(5000)))
                .receiverBalance(otherAccount.getBalance().add(BigDecimal.valueOf(5000)))
                .build();


        Transaction withdrawTransaction = Transaction.builder()
                .transactionId(BigInteger.valueOf(2))
                .amount(BigDecimal.valueOf(2000))
                .transactionAt(LocalDateTime.now())
                .senderAccountId(otherAccount)
                .receiverAccountId(userAccount)
                .senderBalance(otherAccount.getBalance().subtract(BigDecimal.valueOf(2000)))
                .receiverBalance(userAccount.getBalance().add(BigDecimal.valueOf(2000)))
                .build();

        return List.of(depositTransaction, withdrawTransaction);
    };


}
