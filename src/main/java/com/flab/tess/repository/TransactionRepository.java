package com.flab.tess.repository;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
    List<Transaction> findBySenderAccountId(Account senderAccount);
    List<Transaction> findByReceiverAccountId(Account receiveAccount);
}
