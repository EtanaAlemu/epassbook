package com.dxvalley.epassbook.serviceImpl;

import org.springframework.stereotype.Service;

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

        Users user = userRepository.findByUserId(userId);
        account.setUser(user);
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
