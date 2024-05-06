package com.flab.tess.docs.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.tess.controller.TransactionController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.dto.*;
import com.flab.tess.service.TransactionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mock;

public class TransactionControllerTest extends RestDocsTest {

    private final TransactionService transactionService = mock(TransactionService.class);

    @Override
    protected Object initializeController() {
        return new TransactionController(transactionService);
    }

    @Test
    void 계좌별_전체_거래_내역_조회() throws Exception{

        //given
        AccountDto testSender = new AccountDto(BigInteger.valueOf(10),"123456","OneTest","입출금",BigDecimal.valueOf(10000));
        ReceiveAccountDto testReceiver = new ReceiveAccountDto(BigInteger.valueOf(11),"77777");


        //given
        List<TransactionDto> testTransactions = new ArrayList<>();
        testTransactions.add(new TransactionDto(
                BigInteger.valueOf(1),
                BigDecimal.valueOf(7777),
                LocalDateTime.now(),
                testSender,
                testReceiver
        ));

        given(transactionService.getTransactionAll(BigInteger.valueOf(10))).willReturn(testTransactions);

        //when&then
        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                .get("/transaction/accounts/{accountId}",10))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("get-transaction-account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("accountId").description("계좌 id")),
                        responseFields(
                                fieldWithPath("[].transactionId").description("거래 내역 id"),
                                fieldWithPath("[].amount").description("거래 금액"),
                                fieldWithPath("[].transactionAt").description("거래 시간"),
                                fieldWithPath("[].sender.accountId").description("계좌 id"),
                                fieldWithPath("[].sender.accountNum").description("계좌 번호"),
                                fieldWithPath("[].sender.accountName").description("계좌 이름"),
                                fieldWithPath("[].sender.accountType").description("계좌 타입"),
                                fieldWithPath("[].sender.balance").description("잔액"),
                                fieldWithPath("[].receiver.accountId").description("상대 계좌 id"),
                                fieldWithPath("[].receiver.accountNum").description("상대 계좌 번호")
                        )));

    }

    @Test
    void 거래_내역_조회() throws Exception{

        //given
        WithdrawalRequestDto testRequest = new WithdrawalRequestDto(BigInteger.valueOf(1), "123456",BigDecimal.valueOf(2000));

        AccountDto sender= new AccountDto(BigInteger.valueOf(1),"88888","OneTest","입출금",BigDecimal.valueOf(10000));
        ReceiveAccountDto receiver = new ReceiveAccountDto(BigInteger.valueOf(11), "123456");

        TransactionDto testTransactionDto = new TransactionDto(
                BigInteger.valueOf(1),
                testRequest.getAmount(),
                LocalDateTime.now(),
                sender,
                receiver
        );

//        given(transactionService.saveTransaction(any(WithdrawalRequestDto.class))).willReturn(testTransactionDto.getTransactionId());
        given(transactionService.getTransaction(BigInteger.valueOf(1))).willReturn(testTransactionDto);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/transaction/{transactionId}",1))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //문서화
                .andDo(document("get-transaction-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("transactionId").description("거래 내역 ID")),
                        responseFields(
                                fieldWithPath("transactionId").description("거래 내역 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("amount").description("거래 금액").type(JsonFieldType.NUMBER),
                                fieldWithPath("transactionAt").description("거래 일시").type(JsonFieldType.ARRAY),
                                fieldWithPath("sender.accountId").description("송신자 계좌 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("sender.accountNum").description("송신자 계좌 번호").type(JsonFieldType.STRING),
                                fieldWithPath("sender.accountName").description("계좌 명의").type(JsonFieldType.STRING),
                                fieldWithPath("sender.accountType").description("계좌 유형").type(JsonFieldType.STRING),
                                fieldWithPath("sender.balance").description("계좌 잔액").type(JsonFieldType.NUMBER),
                                fieldWithPath("receiver.accountId").description("수신자 계좌 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("receiver.accountNum").description("수신자 계좌 번호").type(JsonFieldType.STRING)
                        )));
    }


    @Test
    void 송금_하기() throws Exception{

        //given

        WithdrawalRequestDto testRequestDto = WithdrawalRequestDto.builder()
                .sendAccountId(BigInteger.valueOf(1))
                .receiveAccountNum("123456")
                .amount(BigDecimal.valueOf(2000))
                .build();

        WithdrawResponseDto withdrawResponseDto = new WithdrawResponseDto(BigInteger.valueOf(1), BigDecimal.valueOf(2000));

        given(transactionService.saveTransaction(any(WithdrawalRequestDto.class))).willReturn(withdrawResponseDto);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/transaction/send")
                .content( objectMapper.writeValueAsString(testRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                 //문서화
                .andDo(document("send-transaction",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("transactionId").description("거래내역 ID").type(JsonFieldType.NUMBER),
                        fieldWithPath("amount").description("거래금액").type(JsonFieldType.NUMBER)
                )));

    }


}
