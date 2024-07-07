package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import com.flab.tess.dto.AccountCreateRequest;
import com.flab.tess.dto.AccountResponse;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@EnableCaching
@Service
@RequiredArgsConstructor //final 에 있는 애로 생성자 만들어줌(lombok 기능)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccountOne(BigInteger accountId){
        return accountRepository.findById(accountId).orElse(null);
    }

//    @Cacheable(value="accountCache", key = "#user")
    public List<Account> getAccounts(User user){
        log.info("accounts cache miss! " + user.getLoginId());
        return accountRepository.findByUser(user);
    }

    @Cacheable(value = "accountCache", key = "#user.userId")
    public List<AccountResponse> getCachedAccountResponses(User user) {
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
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
