package com.dxvalley.epassbook.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.repositories.UserRepository;

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
    List<Account> getUsers(@PathVariable String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        var accounts = user.getAccounts();
        List<Account> updateAccounts = new ArrayList<>();
        for (var account : accounts) {
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

            account.setBalance(balance);
            updateAccounts.add(account);
        }

        return updateAccounts;
    }

    private String getAccountBalance(String accountNumber) {
        
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        
            String reqBody = "{\"BalanceEnquiryRequest\":{\"ESBHeader\":{\"serviceCode\":\"300000\",\"channel\":\"USSD\",\"Service_name\":\"BalanceEnquiry\",\"Message_Id\":\"6255726662\"},\"WebRequestCommon\":{\"company\":\"ET0010222\",\"password\":\"123456\",\"userName\":\"MMTUSER1\"},\"ACCTBALCTSType\":[{\"columnName\":\"ACCOUNT.NUMBER\",\"criteriaValue\":\"" +accountNumber+"\",\"operand\":\"EQ\"}]}}";

            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("BalanceEnquiryResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();


            if (ESBStatus.getString("Status").equals("Success")) {
                JSONArray MINISTMT = new JSONObject(response.getBody())
                        .getJSONObject("BalanceEnquiryResponse")
                        .getJSONObject("EMMTMINISTMTType")
                        .getJSONObject("gEMMTMINISTMTDetailType")
                        .getJSONArray("mEMMTMINISTMTDetailType");

                resBody.put("status", "success");
                resBody.put("statement", String.valueOf(MINISTMT));
                
                //get account balance here 

                return "132312"; //return the balance
            } else {

                // return ESBStatus.getString("errorDescription");
                return "";
            }

        } catch (Exception e) {
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

    @GetMapping("/getPrimaryAccount/{phoneNumber}/{passcode}")
    public ResponseEntity<?> getPrimaryAccount(@PathVariable String phoneNumber, @PathVariable Integer passcode) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ResourceNotFoundException("There is no user with this phoneNumber");
        }

        var accounts = user.getAccounts();
        Account primaryAccount = null;
        for (var account : accounts) {
            if (account.getIsMainAccount() == true)
                primaryAccount = account;
        }

        if (primaryAccount == null) {
            ApiResponse response = new ApiResponse(
                    "not_found",
                    "You don't have a primary account.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!(primaryAccount.getPasscode().equals(passcode))) {
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
