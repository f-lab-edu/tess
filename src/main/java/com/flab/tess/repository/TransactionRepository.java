package com.flab.tess.repository;

import com.flab.tess.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
}
