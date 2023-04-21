package com.dxvalley.epassbook.account;

import com.dxvalley.epassbook.account.Account;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountsResponseDTO {
    private Account primaryAccount;
    private List<Account> accounts = new ArrayList<>();

    public void addToAccounts(Account account){
        accounts.add(account);
    }
}
