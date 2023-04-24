package com.dxvalley.epassbook.account;

import com.dxvalley.epassbook.appConnect.CBOAccountService;
import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.user.UserRepository;
import com.dxvalley.epassbook.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    CBOAccountService cboAccountService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account addAccount(Account account, Long userId) {
        Users users = userRepository.getById(userId);
        return accountRepository.save(account);
    }

    @Override
    public Account changeAccountStatus(String accountNumber, Boolean status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("There is no Account with this Account Number"));
        account.setStatus(status);
        return accountRepository.save(account);
    }

    @Override
    public AccountsResponseDTO getAccountsByUsername(String username) {
        var user = userRepository.findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundException("There is no user with this username."));

        AccountsResponseDTO response = new AccountsResponseDTO();
        for (var account : user.getAccounts()) {
            if (account.getIsMainAccount() == true)
                response.setPrimaryAccount(account);

            account.setBalance(cboAccountService.getAccountBalance(account.getAccountNumber()));
            response.addToAccounts(account);
        }
        return response;
    }

    @Override
    public List<Account> saveAccounts(List<AccountDTO> accountsDTO) {
        ArrayList<Account> accounts = new ArrayList<>();
        for (AccountDTO accountDTO : accountsDTO) {
            Account account = new Account();
            account.setAccountNumber(accountDTO.getAccountNumber());
            account.setAccountTitle(accountDTO.getAccountTitle());
            account.setCoCode(accountDTO.getCoCode());
            account.setBranchName(accountDTO.getBranchName());
            account.setIsMainAccount(false);
            account.setStatus(false);
            accounts.add(account);
        }
        return accountRepository.saveAll(accounts);
    }

    @Override
    public Account setPrimaryAccount(String username, Account tempAccount) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        List<Account> accounts = user.getAccounts();

        Account primaryAccount = accounts.stream()
                .filter(account -> account.getAccountNumber().equals(tempAccount.getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        user.getAccounts().forEach(account -> {
            if (account.equals(primaryAccount)) {
                account.setIsMainAccount(true);
                account.setPasscode(tempAccount.getPasscode());
            } else {
                account.setIsMainAccount(false);
                account.setPasscode(null);
            }
        });

        accountRepository.saveAll(accounts);

        return primaryAccount;
    }

    @Override
    public ResponseEntity<?> getPrimaryAccount(PrimaryAccount primaryAccountRequest) {
        Users user = userRepository.findByPhoneNumber(primaryAccountRequest.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Optional<Account> optionalPrimaryAccount = user.getAccounts().stream()
                .filter(Account::getIsMainAccount)
                .findFirst();

        if (optionalPrimaryAccount.isEmpty())
            throw new ResourceNotFoundException("No primary account found. Please set up a primary account using CooPass.");

        Account primaryAccount = optionalPrimaryAccount.get();
        if (!primaryAccount.getPasscode().equals(primaryAccountRequest.getPasscode()))
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "Invalid passcode!");

        PrimaryAccount response = new PrimaryAccount();
        response.setAccountNumber(primaryAccount.getAccountNumber());
        response.setPasscode(primaryAccount.getPasscode());
        response.setPhoneNumber(user.getPhoneNumber());

        return ApiResponse.success(response);
    }
}
