package com.flab.tess.service;

import com.flab.tess.domain.Account;
import com.flab.tess.dto.AccountDto;
import com.flab.tess.repository.AccountRepository;
import com.flab.tess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor //final 에 있는 애로 생성자 만들어줌(lombok 기능)
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountDto getAccountOne(BigInteger accountId){
        Optional<Account> account = accountRepository.findById(accountId);
        AccountDto accountDto = AccountDto.from(account.get());
        return accountDto;
    }

    public List<AccountDto> getAccountAll(){
        List<Account> accountList = accountRepository.findAll();

        List<AccountDto> accountDtoList = new ArrayList<>();

        for(Account account : accountList){
            AccountDto accountDto = AccountDto.from(account);
            accountDtoList.add(accountDto);
        }

        return accountDtoList;
    }


}
