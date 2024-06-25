package com.flab.tess.domain;
import static org.junit.jupiter.api.Assertions.*;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class TransactionTest {

    private Account receiverAccount;
    private Account senderAccount;

    @BeforeEach
    void setUp() {
        receiverAccount = Account.builder()
                .accountId(BigInteger.valueOf(1))
                .accountNum("123456")
                .accountName("Receiver Account")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(5000))
                .build();

        senderAccount = Account.builder()
                .accountId(BigInteger.valueOf(2))
                .accountNum("654321")
                .accountName("Sender Account")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(10000))
                .build();
    }

    @Test
    void 트랜잭션_팩토리메서드_객체_생성_확인() {
        BigDecimal amount = BigDecimal.valueOf(1500);
        BigDecimal receiverBalance = receiverAccount.getBalance().add(amount);
        BigDecimal senderBalance = senderAccount.getBalance().subtract(amount);

        Transaction transaction = Transaction.of(amount, receiverAccount, senderAccount, receiverBalance, senderBalance);

        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(receiverAccount, transaction.getReceiverAccountId());
        assertEquals(senderAccount, transaction.getSenderAccountId());
        assertEquals(receiverBalance, transaction.getReceiverBalance());
        assertEquals(senderBalance, transaction.getSenderBalance());
    }

    @Test
    void 트랜잭션_거래내역_현재시간으로_저장_확인() {
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(1500))
                .receiverAccountId(receiverAccount)
                .senderAccountId(senderAccount)
                .receiverBalance(BigDecimal.valueOf(6500))
                .senderBalance(BigDecimal.valueOf(8500))
                .build();

        transaction.transactionAt();

        assertNotNull(transaction.getTransactionAt());
        assertTrue(transaction.getTransactionAt().isBefore(LocalDateTime.now()) || transaction.getTransactionAt().isEqual(LocalDateTime.now()));
    }
}