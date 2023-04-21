package com.dxvalley.epassbook.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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