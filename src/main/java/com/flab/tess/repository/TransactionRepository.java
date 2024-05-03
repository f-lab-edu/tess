package com.flab.tess.repository;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.Transaction;
import com.flab.tess.dto.TransactionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
    List<Transaction> findBySenderAccountId(Account senderAccount);
}
