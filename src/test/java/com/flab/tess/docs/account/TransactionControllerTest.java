package com.flab.tess.docs.account;

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

    private User createUser() {
        User user = new User();
        // 필요한 필드 설정
        return user;
    }

    static Principal mockPrincipal = new Principal() {
        @Override
        public String getName() {
            return "testUser";
        }
    };

//    @Test
//    void 계좌별_전체_거래_내역_조회() throws Exception{
//
//        // Mock Account 객체 생성 및 설정
//        Account mockSender = mock(Account.class);
//        when(mockSender.getAccountId()).thenReturn(BigInteger.valueOf(10));
//        when(mockSender.getAccountNum()).thenReturn("77777");
//        when(mockSender.getAccountName()).thenReturn("유정");
//        when(mockSender.getAccountType()).thenReturn("입출금");
//        when(mockSender.getBalance()).thenReturn(BigDecimal.valueOf(7777700));
//
//        Account mockReceiver = mock(Account.class);
//        when(mockReceiver.getAccountId()).thenReturn(BigInteger.valueOf(11));
//        when(mockReceiver.getAccountNum()).thenReturn("123456");
//
//        // Mock Transaction 객체 생성 및 설정
//        Transaction mockTransaction1 = mock(Transaction.class);
//        when(mockTransaction1.getTransactionId()).thenReturn(BigInteger.valueOf(1));
//        when(mockTransaction1.getAmount()).thenReturn(BigDecimal.valueOf(7777));
//        when(mockTransaction1.getTransactionAt()).thenReturn(LocalDateTime.now());
//        when(mockTransaction1.getSenderAccountId()).thenReturn(mockSender);
//        when(mockTransaction1.getReceiverAccountId()).thenReturn(mockReceiver);
//
//        Transaction mockTransaction2 = mock(Transaction.class);
//        when(mockTransaction2.getTransactionId()).thenReturn(BigInteger.valueOf(2));
//        when(mockTransaction2.getAmount()).thenReturn(BigDecimal.valueOf(11111));
//        when(mockTransaction2.getTransactionAt()).thenReturn(LocalDateTime.now());
//        when(mockTransaction2.getSenderAccountId()).thenReturn(mockSender);
//        when(mockTransaction2.getReceiverAccountId()).thenReturn(mockReceiver);
//
//        given(transactionService.getTransactionAll(BigInteger.valueOf(10))).willReturn(Arrays.asList(mockTransaction1, mockTransaction2));
//
//        //when&then
//        this.mockMvc.perform(
//                RestDocumentationRequestBuilders
//                .get("/transactions/accounts/{accountId}",10))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andDo(document("get-transaction-account",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(parameterWithName("accountId").description("계좌 id")),
//                        responseFields(
//                                fieldWithPath("[].transactionId").description("거래 내역 id").type(JsonFieldType.NUMBER).optional(),
//                                fieldWithPath("[].amount").description("거래 금액").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("[].transactionAt").description("거래 시간").type(JsonFieldType.ARRAY).optional(),
//                                fieldWithPath("[].sender.accountId").description("계좌 id").type(JsonFieldType.NUMBER).optional(),
//                                fieldWithPath("[].sender.accountNum").description("계좌 번호").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("[].sender.accountName").description("계좌 이름").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("[].sender.accountType").description("계좌 타입").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("[].sender.balance").description("잔액").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("[].receiver.accountId").description("상대 계좌 id").type(JsonFieldType.NUMBER).optional(),
//                                fieldWithPath("[].receiver.accountNum").description("상대 계좌 번호").type(JsonFieldType.STRING).optional()
//                        )));
//
//    }

//    @Test
//    void 거래_내역_상세_조회() throws Exception{
//
//        //given
//        User testUser = createUser();
//        given(customUserDetailService.findUser(any(Principal.class))).willReturn(testUser);
//
//        Transaction mockTransaction = mock(Transaction.class);
//        when(mockTransaction.getTransactionId()).thenReturn(BigInteger.valueOf(1));
//        when(mockTransaction.getAmount()).thenReturn(BigDecimal.valueOf(10000));  // 예시 금액
//        when(mockTransaction.getTransactionAt()).thenReturn(LocalDateTime.now());  // 예시 거래 일시
//
//        // Sender와 Receiver 정보를 포함하는 내부 클래스나 객체가 있다고 가정
//        Account sender = mock(Account.class);
//        Account receiver = mock(Account.class);
//
//        // Sender 모의 설정
//        when(sender.getAccountId()).thenReturn(BigInteger.valueOf(11));
//        when(sender.getAccountNum()).thenReturn("123456");
//        when(sender.getAccountName()).thenReturn("선재");
//        when(sender.getBalance()).thenReturn(BigDecimal.valueOf(10000));
//
//        // Receiver 모의 설정
//        when(receiver.getAccountId()).thenReturn(BigInteger.valueOf(12));
//        when(receiver.getAccountNum()).thenReturn("4567891");
//
//        // Mock Transaction 객체에 Sender와 Receiver 설정
//        when(mockTransaction.getSenderAccountId()).thenReturn(sender);
//        when(mockTransaction.getReceiverAccountId()).thenReturn(receiver);
//
//        given(transactionService.getTransaction(BigInteger.valueOf(1))).willReturn(mockTransaction);
//
//        //when & then
//        mockMvc.perform(RestDocumentationRequestBuilders
//                .get("/transactions/{transactionId}",1)
//                .principal(mockPrincipal))
//                .andExpect(content().contentType("application/json"))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                //문서화
//                .andDo(document("get-transaction-one",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(parameterWithName("transactionId").description("거래 내역 ID")),
//                        responseFields(
//                                fieldWithPath("transactionId").description("거래 내역 ID").type(JsonFieldType.NUMBER).optional(),
//                                fieldWithPath("amount").description("거래 금액").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("transactionAt").description("거래 일시").type(JsonFieldType.ARRAY).optional(),
//                                fieldWithPath("transactionType").description("거래 타입").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("userInfo.accountName").description("user 계좌명").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("userInfo.accountNum").description("user 계좌 번호").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("userInfo.balance").description("거래 후 계좌 잔액").type(JsonFieldType.STRING).optional(),
//                                fieldWithPath("otherAccountName").description("거래 상대 계좌명").type(JsonFieldType.STRING).optional()
//                        )));
//    }


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


}
