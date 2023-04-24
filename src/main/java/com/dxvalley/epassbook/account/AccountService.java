package com.dxvalley.epassbook.account;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    Account addAccount(Account account, Long userId);

    Account changeAccountStatus(String accountNumber, Boolean status);

    ResponseEntity getPrimaryAccount(PrimaryAccount primaryAccountRequest);

    Account setPrimaryAccount(String username, Account tempAccount);

    AccountsResponseDTO getAccountsByUsername(String username);

    List<Account> saveAccounts(List<AccountDTO> accounts);
}

