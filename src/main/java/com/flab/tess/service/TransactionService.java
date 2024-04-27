package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.AccountDto;
import com.flab.tess.dto.ReceiveAccountDto;
import com.flab.tess.dto.SendDto;
import com.flab.tess.dto.TransactionDto;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.TransactionRepository;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BigInteger saveTransaction(SendDto sendDto){

        BigInteger sender = sendDto.getSendAccountId();
        String receiveAccountNum = sendDto.getReceiveAccountNum();
        BigDecimal amount = sendDto.getAmount();

        //거래 시간 현재시간으로 생성 후 저장
        Transaction transaction = new Transaction();
        transaction.setTransactionAt(Timestamp.valueOf(LocalDateTime.now()));

        //거래 금액 저장
        transaction.setAmount(amount);

        //받는 사람 계좌, accountId
        Account receiveAccount = accountRepository.findByAccountNum(receiveAccountNum);
        BigInteger receiver = receiveAccount.getAccountId();

        //보내는 사람 계좌
        Optional<Account> sendAccount = accountRepository.findById(sender);

        //받는 사람, 보내는 사람 계좌 잔액 업데이트
        updateBalance(amount, sender, receiver);

        transaction.setReceiverAccountId(receiveAccount);
        transaction.setSenderAccountId(sendAccount.get());


        transactionRepository.save(transaction);
        return transaction.getTransactionId();
    }

    public void updateBalance(BigDecimal amount, BigInteger sender, BigInteger receiver){

        //보내는 사람 계좌 잔액(balance)에서 보낸 만큼(amount) 빼줌
        Optional<Account> sendAccount = accountRepository.findById(sender);
        BigDecimal sendBalance = sendAccount.get().getBalance();
        sendBalance=sendBalance.subtract(amount);
        sendAccount.get().setBalance(sendBalance);
        accountRepository.save(sendAccount.get());

        //받는 사람 계좌 잔액(balance)에서 받는 만큼(amount) 더해줌
        Optional<Account> receiveAccount = accountRepository.findById(receiver);
        BigDecimal receiveBalance = receiveAccount.get().getBalance();
        receiveBalance=receiveBalance.add(amount);
        receiveAccount.get().setBalance(receiveBalance);
        accountRepository.save(receiveAccount.get());

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
