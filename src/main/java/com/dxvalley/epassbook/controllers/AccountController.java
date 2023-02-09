package com.dxvalley.epassbook.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.serviceImpl.AccountServiceImpl;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private AccountServiceImpl accountService;
    private AccountRepository accountRepository;

    public AccountController(AccountServiceImpl accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/getAccounts")
    List<Account> getUsers() {
        return accountRepository.findAll();
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Account> addAccount(@RequestBody Account account, @PathVariable Long userId) {
        Account newAccount = accountService.addAccount(account, userId);

        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

}
