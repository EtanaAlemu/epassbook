package com.dxvalley.epassbook.account;

import com.dxvalley.epassbook.account.dto.AccountDTO;
import com.dxvalley.epassbook.account.dto.AccountNumberDTO;
import com.dxvalley.epassbook.account.dto.AccountsResponseDTO;
import com.dxvalley.epassbook.account.dto.PrimaryAccountDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    Account addAccount(Account account, Long userId);

    Account changeAccountStatus(String accountNumber, Boolean status);

    ResponseEntity getPrimaryAccount(PrimaryAccountDTO primaryAccountRequest);

    Account setPrimaryAccount(String username, Account tempAccount);

    AccountsResponseDTO getAccountsByUsername(String username);

    List<Account> saveAccounts(List<AccountDTO> accounts);

    AccountNumberDTO getAccountByPhoneNumber(String phoneNumber);
}

