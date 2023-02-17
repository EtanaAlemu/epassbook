package com.dxvalley.epassbook.controllers;

import java.util.List;

import com.dxvalley.epassbook.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.serviceImpl.AccountServiceImpl;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private AccountServiceImpl accountService;
    private AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountController(AccountServiceImpl accountService, AccountRepository accountRepository,
                             UserRepository userRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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

    @PutMapping("setPrimaryAccount/{userId}")
    public ResponseEntity<?> setPrimaryAccount(@RequestBody Account tempAccount, @PathVariable Long userId) {
        var accounts = accountRepository.findByUser(userId);

        for(var account : accounts){
            if(account.getAccountNumber() == tempAccount.getAccountNumber()){
                account.setIsMainAccount(true);
                account.setPasscode(tempAccount.getPasscode());
                continue;
            }
            account.setIsMainAccount(false);
        }
        accountRepository.saveAll(accounts);
        return new ResponseEntity<>(
                "Your primary account has been successfully set.",
                HttpStatus.OK);
    }

}
