package com.dxvalley.epassbook.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dxvalley.epassbook.dto.AccountResponseDTO;
import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.dto.PrimaryAccount;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.repositories.UserRepository;

import jakarta.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @GetMapping("/getAccounts/{phoneNumber}")
    ResponseEntity<?> getUsers(@PathVariable String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        List<Account> accounts = new ArrayList<>();
        AccountResponseDTO response = new AccountResponseDTO();

        for (var account : user.getAccounts()) {
            // query balance here from CBS
            String balance;

            //check if balance response is correct here if not set balance to ""
            // if(Integer.parseInt(balance))

            balance = getAccountBalance(account.getAccountNumber());
            // try {
            //     balance = getAccountBalance(account.getAccountNumber());
            //     Integer.parseInt(balance);
            // } catch (NumberFormatException e) {
            //     balance = "";
            // }

            if (account.getIsMainAccount() == true) {
                PrimaryAccount primaryAccount = new PrimaryAccount();
                primaryAccount.setAccountNumber(account.getAccountNumber());
                primaryAccount.setPasscode(account.getPasscode());
                primaryAccount.setPhoneNumber(user.getPhoneNumber());
                response.setPrimaryAccount(primaryAccount);
            }
            account.setBalance(balance);
            accounts.add(account);
        }

        response.setAccounts(accounts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PatchMapping("/updateStatus/{accountNumber}/{status}")
    ResponseEntity<?> updateStatus(@PathVariable String accountNumber, @PathVariable Boolean status) {
        var account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new ResourceNotFoundException("There is no Account with this Account Number");
        }
        account.setStatus(status);
        accountRepository.save(account);
        ApiResponse response = new ApiResponse("success", "Updated Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private String getAccountBalance(String accountNumber) {

        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String reqBody = "{\"BalanceEnquiryRequest\":{\"ESBHeader\":{\"serviceCode\":\"300000\",\"channel\":\"USSD\",\"Service_name\":\"BalanceEnquiry\",\"Message_Id\":\"6255726662\"},\"WebRequestCommon\":{\"company\":\"ET0010222\",\"password\":\"123456\",\"userName\":\"MMTUSER1\"},\"ACCTBALCTSType\":[{\"columnName\":\"ACCOUNT.NUMBER\",\"criteriaValue\":\"" + accountNumber + "\",\"operand\":\"EQ\"}]}}";

            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("BalanceEnquiryResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();

            if (ESBStatus.getString("status").equals("Success")) {
                String balance = new JSONObject(response.getBody())
                        .getJSONObject("BalanceEnquiryResponse")
                        .getJSONObject("ACCTBALCTSType")
                        .getJSONObject("gACCTBALCTSDetailType")
                        .getJSONObject("mACCTBALCTSDetailType")
                        .getJSONObject("mACCTBALCTSDetailType")
                        .getString("WorkingBal");

                resBody.put("status", "success");
                resBody.put("statement", String.valueOf(balance));

                return balance; //return the balance
            } else {

                // return ESBStatus.getString("errorDescription");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
            // return e.toString();
        }
    }


    @PostMapping("/{userId}")
    public ResponseEntity<Account> addAccount(@RequestBody Account account, @PathVariable Long userId) {
        Account newAccount = accountService.addAccount(account, userId);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping("/setPrimaryAccount/{phoneNumber}")
    public ResponseEntity<?> setPrimaryAccount(@PathVariable String phoneNumber, @RequestBody Account tempAccount) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        var accounts = user.getAccounts();
        for (var account : accounts) {
            if (tempAccount.getAccountNumber().equals(account.getAccountNumber())) {
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

    @PostMapping("/getPrimaryAccount")
    public ResponseEntity<?> getPrimaryAccount(@RequestBody @Valid PrimaryAccount primaryAccountRequest) {
        var user = userRepository.findByPhoneNumber(primaryAccountRequest.getPhoneNumber());
        if (user == null) {
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        PrimaryAccount primaryAccount = new PrimaryAccount();
        for (var account : user.getAccounts()) {
            if (account.getIsMainAccount() == true) {
                if (account.getPasscode().equals(primaryAccountRequest.getPasscode())) {
                    primaryAccount.setAccountNumber(account.getAccountNumber());
                    primaryAccount.setPasscode(account.getPasscode());
                    primaryAccount.setPhoneNumber(user.getPhoneNumber());
                    return new ResponseEntity<>(primaryAccount, HttpStatus.OK);
                }
                ApiResponse response = new ApiResponse("unauthorized","Invalid passcode!");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }
            }
        throw new ResourceNotFoundException("You don't have a primary account");
    }
}