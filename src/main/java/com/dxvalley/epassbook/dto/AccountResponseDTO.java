package com.dxvalley.epassbook.dto;

import com.dxvalley.epassbook.account.Account;
import com.dxvalley.epassbook.account.PrimaryAccount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountResponseDTO {
    private PrimaryAccount primaryAccount;
    private List<Account> accounts = new ArrayList<>();
}
