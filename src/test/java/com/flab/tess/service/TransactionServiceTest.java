package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.domain.User;
import com.flab.tess.dto.WithdrawalRequestDto;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account sendAccount;
    private Account receiveAccount;
    private WithdrawalRequestDto withdrawalRequestDto;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        sendAccount = Account.builder()
                .accountId(BigInteger.valueOf(11))
                .accountNum("123456")
                .accountName("유정 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(25000))
                .build();

        receiveAccount = Account.builder()
                .accountId(BigInteger.valueOf(12)) // 다른 값으로 변경
                .accountNum("7777")
                .accountName("행운의 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(5000))
                .build();

        withdrawalRequestDto = new WithdrawalRequestDto("11", "7777", "5000");
    }


    @Test
    void 거래_내역_저장(){

        // stub
        given(accountRepository.findByAccountNum(withdrawalRequestDto.getReceiveAccountNum())).willReturn(Optional.of(receiveAccount));
        given(accountRepository.findById(new BigInteger(withdrawalRequestDto.getSendAccountId()))).willReturn(Optional.of(sendAccount));

        // when
        Transaction resultTransaction = transactionService.saveTransaction(withdrawalRequestDto);

        // then
        assertEquals(new BigDecimal(10000), resultTransaction.getReceiverAccountId().getBalance());
        assertEquals(new BigDecimal(20000), resultTransaction.getSenderAccountId().getBalance());

        assertEquals(new BigDecimal(withdrawalRequestDto.getAmount()), resultTransaction.getAmount());
        assertNotNull(resultTransaction);
    }

    @Test
    void 수신_계좌_없음_예외(){
        // Given
        when(accountRepository.findByAccountNum(receiveAccount.getAccountNum())).thenReturn(Optional.empty());
        when(accountRepository.findById(sendAccount.getAccountId())).thenReturn(Optional.of(sendAccount));

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.saveTransaction(withdrawalRequestDto);
        });

        assertEquals("수신 계좌가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 송신_계좌_없음_예외(){
        // Given
        when(accountRepository.findByAccountNum(receiveAccount.getAccountNum())).thenReturn(Optional.of(receiveAccount));
        when(accountRepository.findById(sendAccount.getAccountId())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.saveTransaction(withdrawalRequestDto);
        });

        assertEquals("송신 계좌가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 거래_내역_상세_조회(){

        //Given
        BigInteger transactionId = BigInteger.valueOf(1);
        Transaction transaction = mock(Transaction.class);

        // stub
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(transaction));

        // when
        Transaction result = transactionService.getTransaction(transactionId);

        // then
        assertNotNull(result);
        assertEquals(transaction, result);
    }

    @Test
    void 출금_내역_조회(){

        // Given
        BigInteger accountId = BigInteger.valueOf(1);
        Account account = mock(Account.class);
        Transaction transaction1 = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        List<Transaction> depositTransactions = Arrays.asList(transaction1, transaction2);

        // stub
        given(accountRepository.findById(accountId)).willReturn((Optional.of(account)));
        given(transactionRepository.findBySenderAccountId(account)).willReturn(depositTransactions);

        // when
        List<Transaction> result = transactionService.getTransactionDeposit(accountId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void 입금_내역_조회(){

        //Given
        BigInteger accountId = BigInteger.valueOf(1);
        Account account = mock(Account.class);
        Transaction transaction1 = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        List<Transaction> withdrawTransactions = Arrays.asList(transaction1, transaction2);

        //stub
        given(accountRepository.findById(accountId)).willReturn(Optional.of(account));
        given(transactionRepository.findByReceiverAccountId(account)).willReturn(withdrawTransactions);

        // when
        List<Transaction> result = transactionService.getTransactionWithdraw(accountId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }


}
