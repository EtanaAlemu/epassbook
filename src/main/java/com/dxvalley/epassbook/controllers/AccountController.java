package com.dxvalley.epassbook.controllers;

import com.dxvalley.epassbook.repository.AccountRepository;

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
