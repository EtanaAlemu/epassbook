package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.models.Account;

public interface AccountService {
    Account addAccount(Account account, Long userId);
    Account updateAccount(Long id, Account newAccount, Long userId);
    Account getMainAccount(Long userId);
}

