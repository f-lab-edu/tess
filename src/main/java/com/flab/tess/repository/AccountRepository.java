package com.flab.tess.repository;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, BigInteger>{
    List<Account> findByUser(User user);
    Optional<Account> findByAccountNum(String accountNum);
}
