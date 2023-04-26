package com.dxvalley.epassbook.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    private String accountNumber;
    private String accountTitle;
    private String coCode;
    private String branchName;
    private Boolean isMainAccount;
    private Integer passcode;
    private String balance;
    private Boolean status;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}