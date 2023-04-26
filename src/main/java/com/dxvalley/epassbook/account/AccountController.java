package com.dxvalley.epassbook.account;

import com.dxvalley.epassbook.account.dto.PrimaryAccountDTO;
import com.dxvalley.epassbook.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccounts/{username}")
    ResponseEntity<?> getAccounts(@PathVariable String username) {
        return ApiResponse.success(accountService.getAccountsByUsername(username));
    }

    @GetMapping("/getByPhoneNumber/{phoneNumber}")
    ResponseEntity<?> getAccountByPhoneNumber(@PathVariable String phoneNumber) {
        return ApiResponse.success(accountService.getAccountByPhoneNumber(phoneNumber));
    }

    @PatchMapping("/changeAccountStatus/{accountNumber}/{status}")
    ResponseEntity<?> changeAccountStatus(@PathVariable String accountNumber, @PathVariable Boolean status) {
        return ApiResponse.success(accountService.changeAccountStatus(accountNumber, status));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addAccount(@RequestBody Account account, @PathVariable Long userId) {
        return ApiResponse.created(accountService.addAccount(account, userId));
    }

    @PutMapping("/setPrimaryAccount/{username}")
    public ResponseEntity<?> setPrimaryAccount(@PathVariable String username, @RequestBody Account tempAccount) {
        return ApiResponse.success(accountService.setPrimaryAccount(username, tempAccount));
    }

    @PostMapping("/getPrimaryAccount")
    public ResponseEntity<?> getPrimaryAccount(@RequestBody @Valid PrimaryAccountDTO primaryAccountRequest) {
        return accountService.getPrimaryAccount(primaryAccountRequest);
    }
}