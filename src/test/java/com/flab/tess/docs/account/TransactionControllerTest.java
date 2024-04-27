package com.flab.tess.docs.account;

import com.flab.tess.controller.TransactionController;
import com.flab.tess.docs.RestDocsTest;
import com.flab.tess.dto.AccountDto;
import com.flab.tess.dto.ReceiveAccountDto;
import com.flab.tess.dto.SendDto;
import com.flab.tess.dto.TransactionDto;
import com.flab.tess.service.TransactionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.mock;

public class TransactionControllerTest extends RestDocsTest {

    private final TransactionService transactionService = mock(TransactionService.class);

    @Override
    protected Object initializeController() {
        return new TransactionController(transactionService);
    }

    @Test
    void 거래_내역_조회() throws Exception{
        //given
        SendDto sendDto = new SendDto(BigInteger.valueOf(1), "123456", BigDecimal.valueOf(2000));

        AccountDto sender= new AccountDto(BigInteger.valueOf(1),"88888","OneTest","입출금",BigDecimal.valueOf(10000));
        ReceiveAccountDto receiver = new ReceiveAccountDto(BigInteger.valueOf(11), "123456");

        TransactionDto testTransactionDto = new TransactionDto(
                BigInteger.valueOf(1),
                sendDto.getAmount(),
                Timestamp.valueOf(LocalDateTime.now()),
                sender,
                receiver
        );

        given(transactionService.saveTransaction(any(SendDto.class))).willReturn(testTransactionDto.getTransactionId());
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
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.transactionId").description("거래 내역 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.amount").description("거래 금액").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.transactionAt").description("거래 일시 - 밀리초 단위의 UNIX 타임스탬프").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.sender.accountId").description("송신자 계좌 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.sender.accountNum").description("송신자 계좌 번호").type(JsonFieldType.STRING),
                                fieldWithPath("data.sender.accountName").description("계좌 명의").type(JsonFieldType.STRING),
                                fieldWithPath("data.sender.accountType").description("계좌 유형").type(JsonFieldType.STRING),
                                fieldWithPath("data.sender.balance").description("계좌 잔액").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.receiver.accountId").description("수신자 계좌 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.receiver.accountNum").description("수신자 계좌 번호").type(JsonFieldType.STRING)
                        )));
    }


    @Test
    void 송금_하기() throws Exception{

        //given
        SendDto sendDto = new SendDto(BigInteger.valueOf(1), "123456", BigDecimal.valueOf(2000));

        given(transactionService.saveTransaction(any(SendDto.class))).willReturn(BigInteger.valueOf(1));

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/transaction/send")
                .content( objectMapper.writeValueAsString(sendDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                 //문서화
                .andDo(document("send-transaction",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("status").description("응답 상태 코드"),
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("transactionId").description("거래내역 ID").type(JsonFieldType.NUMBER)
                )));

    }


}
