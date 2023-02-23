package com.dxvalley.epassbook.serviceImpl;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.repositories.UserRepository;

import org.springframework.stereotype.Service;
import com.dxvalley.epassbook.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Account addAccount(Account account, Long userId) {
        Users users = userRepository.getById(userId);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account newAccount, Long userId) {

        return null;
    }

    @Override
    public Account getMainAccount(Long userId) {

        return null;
    }

}
