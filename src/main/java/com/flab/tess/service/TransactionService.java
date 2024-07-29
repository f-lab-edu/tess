package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.*;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction saveTransaction(WithdrawalRequestDto withdrawalRequestDto){

        BigInteger sender = new BigInteger(withdrawalRequestDto.getSendAccountId());
        String receiveAccountNum = withdrawalRequestDto.getReceiveAccountNum();
        BigDecimal amount = new BigDecimal(withdrawalRequestDto.getAmount());

        // 받는 사람의 계좌를 조회
        Account receiveAccount = accountRepository.findByAccountNum(receiveAccountNum)
                .orElseThrow(() -> new IllegalArgumentException("수신 계좌가 존재하지 않습니다."));

        //보내는 사람 계좌 조회
        Account sendAccount = accountRepository.findById(sender)
                .orElseThrow(() -> new IllegalArgumentException("송신 계좌가 존재하지 않습니다."));

        if(receiveAccount.equals(sendAccount)){
            throw new IllegalArgumentException("송신 계좌와 수신 계좌가 같습니다.");
        }

        //받는 사람, 보내는 사람 계좌 잔액 업데이트
        Account proceedReceiveAccount = receiveAccount.deposit(amount);
        Account proceedSenderAccount = sendAccount.withdraw(amount);

        //of 팩토리 메서드를 통해 트랜잭션 객체 생성 및 초기화를 하나의 정적 메소드에서 캡슐화해서 진행
        Transaction transaction = Transaction.of(
                amount,
                proceedReceiveAccount,
                proceedSenderAccount,
                proceedReceiveAccount.getBalance(),
                proceedSenderAccount.getBalance()
        );

        //트랜잭션 객체 DB에 저장
        transactionRepository.save(transaction);

        return transaction;
    }


    public Transaction getTransaction(BigInteger transactionId){
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + transactionId));
    }

    //sender가 user인 내역
    public List<Transaction> getTransactionDeposit(BigInteger accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + accountId));
        return transactionRepository.findBySenderAccountId(account);
    }

    //receiver가 user인 내역
    public List<Transaction> getTransactionWithdraw(BigInteger accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + accountId));
        return transactionRepository.findByReceiverAccountId(account);
    }

}
