package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.dto.AccountCreateRequest;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@EnableCaching
@Service
@RequiredArgsConstructor //final 에 있는 애로 생성자 만들어줌(lombok 기능)
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account getAccountOne(BigInteger accountId){
        return accountRepository.findById(accountId).orElse(null);
    }

    @Cacheable("accounts")
    public List<Account> getAccounts(User user){
        return accountRepository.findByUser(user);
    }

    @Transactional
    public Account saveAccount(AccountCreateRequest accountCreateRequest, User user){
        Account account = Account.of(
                user,
                accountCreateRequest.getAccountNum(),
                accountCreateRequest.getAccountName(),
                accountCreateRequest.getAccountType());

        accountRepository.save(account);
        return account;
    }

}
