package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.*;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.TransactionRepository;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public WithdrawResponseDto saveTransaction(WithdrawalRequestDto withdrawalRequestDto){

        BigInteger sender = withdrawalRequestDto.getSendAccountId();
        String receiveAccountNum = withdrawalRequestDto.getReceiveAccountNum();
        BigDecimal amount = withdrawalRequestDto.getAmount();

        // 받는 사람의 계좌를 조회
        Account receiveAccount = accountRepository.findByAccountNum(receiveAccountNum)
                .orElseThrow(() -> new IllegalArgumentException("수신 계좌가 존재하지 않습니다."));

        //보내는 사람 계좌 조회
        Optional<Account> sendAccount = accountRepository.findById(sender);

        //받는 사람, 보내는 사람 계좌 잔액 업데이트
        receiveAccount.deposit(amount);
        sendAccount.get().withdraw(amount);

        //트랜잭션 객체 생성 및 초기화
        Transaction transaction = new Transaction();
        transaction.transactionAt(LocalDateTime.now());
        transaction.saveAmount(amount);
        transaction.saveReceiver(receiveAccount);
        transaction.saveSender(sendAccount.get());

        //트랜잭션 객체 DB에 저장
        transactionRepository.save(transaction);

        WithdrawResponseDto withdrawResponseDto = new WithdrawResponseDto(transaction.getTransactionId(), transaction.getAmount());

        return withdrawResponseDto;
    }


    public TransactionDto getTransaction(BigInteger transactionId){
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);

        //보내는 사람 계좌
        Account sender = transaction.get().getSenderAccountId();
        AccountDto sendAccountDto = AccountDto.from(sender);

        //받는 사람 계좌
        Account receiver= transaction.get().getReceiverAccountId();
        ReceiveAccountDto receiveAccountDto = ReceiveAccountDto.from(receiver);

        TransactionDto transactionDto = new TransactionDto(transactionId, transaction.get().getAmount(), transaction.get().getTransactionAt(), sendAccountDto, receiveAccountDto);


        return transactionDto;
    }

}
