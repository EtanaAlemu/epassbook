package com.dxvalley.epassbook.controllers;

import java.util.List;

import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.serviceImpl.AccountServiceImpl;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAccounts")
    List<Account> getUsers() {
        return accountRepository.findAll();
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Account> addAccount(@RequestBody Account account, @PathVariable Long userId) {
        Account newAccount = accountService.addAccount(account, userId);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/getPrimaryAccount")
    public ResponseEntity<?> getPrimaryAccount(@RequestParam String phoneNumber, @RequestParam Integer passcode) {
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        var primaryAccount = accountRepository.findPrimaryAccount(user.getUserId());

        if(primaryAccount == null) {
            createUserResponse response = new createUserResponse(
                    "error",
                    "You don't have a primary account!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(primaryAccount.getPasscode() != passcode) {
            createUserResponse response = new createUserResponse(
                    "error",
                    "Invalid passcode!");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(
                primaryAccount,
                HttpStatus.OK);
    }

    @PutMapping("/setPrimaryAccount/{userId}")
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
