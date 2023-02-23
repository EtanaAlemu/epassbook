package com.dxvalley.epassbook.controllers;

import java.util.List;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
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

    @PutMapping("/setPrimaryAccount/{phoneNumber}")
    public ResponseEntity<?> setPrimaryAccount(@PathVariable String phoneNumber, @RequestBody Account tempAccount) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if(user == null){
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        var accounts = user.getAccounts();
        for(var account : accounts){
            if(tempAccount.getAccountNumber().equals(account.getAccountNumber())){
                account.setIsMainAccount(true);
                account.setPasscode(tempAccount.getPasscode());
                continue;
            }
            account.setIsMainAccount(false);
        }
        accountRepository.saveAll(accounts);
        ApiResponse response = new ApiResponse(
                "success",
                "Your primary account has been successfully set.");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    @GetMapping("/getPrimaryAccount/{phoneNumber}/{passcode}")
    public ResponseEntity<?> getPrimaryAccount(@PathVariable String phoneNumber, @PathVariable Integer passcode) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if(user == null){
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        var accounts = user.getAccounts();
        Account primaryAccount = null;
        for(var account : accounts){
            if(account.getIsMainAccount() == true)
                primaryAccount = account;
        }

        if(primaryAccount == null) {
            ApiResponse response = new ApiResponse(
                    "not_found",
                    "You don't have a primary account.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if(!(primaryAccount.getPasscode().equals( passcode))) {
            ApiResponse response = new ApiResponse(
                    "forbidden",
                    "Invalid passcode!");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(
                primaryAccount,
                HttpStatus.OK);
    }

}
