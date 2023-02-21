package com.dxvalley.epassbook.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.repositories.AddressRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.dxvalley.epassbook.models.Address;
import com.dxvalley.epassbook.models.Role;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.RoleRepository;
import com.dxvalley.epassbook.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
        private final UserRepository userRepository;
        private final RoleRepository roleRepo;
        private final PasswordEncoder passwordEncoder;
        private final AccountRepository accountRepository;
        private final AddressRepository addressRepository;

        // get user info
        @GetMapping("/getUserInfo")
        public ResponseEntity<?> getUserInfo(@RequestBody MobileNumber phoneNumber) {
                try {
                        String uri = "http://10.1.245.150:7081/userInfo";
                        RestTemplate restTemplate = new RestTemplate();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        String requestBody = "{\"phoneNumber\":" + phoneNumber.getPhoneNumber() + "}";

                        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                        ResponseEntity<UserInfoResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                                        UserInfoResponse.class);

                        return new ResponseEntity<>(res, HttpStatus.OK);

                } catch (Exception e) {
                        createUserResponse response = new createUserResponse("error",
                                        " Can't find User with this phone Number");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

        @PostMapping("/checkUserExistence")
        public ResponseEntity<?> checkUserExistence(@RequestBody MobileNumber phoneNumber) {

                if (userRepository.findByPhoneNumber(phoneNumber.getPhoneNumber()) != null) {
                        createUserResponse response = new createUserResponse(
                                "error",
                                "This phoneNumber is already used on this platform!");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                ResponseEntity<UserInfoResponse> res;
                try {
                        String uri = "http://10.1.245.150:7081/userInfo";
                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        String requestBody = "{\"phoneNumber\":" + phoneNumber.getPhoneNumber() + "}";
                        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
                        res = restTemplate.exchange(uri, HttpMethod.POST, request, UserInfoResponse.class);
                } catch (Exception e) {
                        createUserResponse response = new createUserResponse(
                                "error",
                                "Can't find User with this phone Number on CBS!");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                Users users = new Users();
                UserInfo userInfo = res.getBody().getUserInfo();

                List<Role> roles = new ArrayList<Role>(1);
                roles.add(this.roleRepo.findByRoleName("user"));

                users.setRoles(roles);
                users.setUsername(phoneNumber.getPhoneNumber());
                users.setFullName(userInfo.getFullName());
                users.setEmail(userInfo.getEmail());
                users.setEmailConfirmed(false);
                users.setBirthDate(userInfo.getDateOfBirth());
                users.setGender(userInfo.getGender());
                users.setImageUrl(userInfo.getImageUrl());
                users.setCreatedAt(LocalDateTime.now().toString());
                users.setAccessFailedCount(0);
                users.setTwoFactorEnabled(false);
                users.setIsEnabled(true);
                users.setPhoneNumber(phoneNumber.getPhoneNumber());
                users.setLanguageCode(0);

                Address address = addressRepository.save(userInfo.getAddress());
                users.setAddress(address);

                ArrayList<Account> accounts = new ArrayList<>();
                for (var account : userInfo.getAccounts()){
                        accounts.add(account);
                }
                List<Account>  accountList = accountRepository.saveAll(accounts);
                users.setAccounts(accountList);

                return new ResponseEntity<>(userRepository.save(users), HttpStatus.CREATED);

        }


        @PutMapping("/setPasswordAndUsername")
        public ResponseEntity<createUserResponse> accept(@RequestBody Users tempUser) {
                var result = userRepository.findByUsername(tempUser.getUsername());
                if (result != null) {
                        createUserResponse response = new createUserResponse("error", "This username is already used.");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                var user = userRepository.findByPhoneNumber(tempUser.getPhoneNumber());
                if(user!=null){
                        user.setUsername(tempUser.getUsername());
                        user.setPassword(passwordEncoder.encode(tempUser.getPassword()));
                        userRepository.save(user);


                        createUserResponse response = new createUserResponse("success", "Updated successfully");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                }


                        createUserResponse response = new createUserResponse("error", "This username is not exist");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }


        @PostMapping("/findAccountsByPhoneNumber")
        public ResponseEntity<?> getAccounts(@RequestBody MobileNumber phoneNumber) {
                try {
                        String uri = "http://10.1.245.150:7081/userInfo";
                        RestTemplate restTemplate = new RestTemplate();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        String requestBody = "{\"phoneNumber\":" + phoneNumber.getPhoneNumber() + "}";

                        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                        ResponseEntity<UserInfoResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                                        UserInfoResponse.class);

                        // for (int i = 0; i < res.getBody().getUserInfo().getAccounts().size(); i++) {
                        // // res.getBody().getUserInfo().getAccounts().add
                        // System.out.println(res.getBody().getUserInfo().getAccounts().get(i));
                        // // accs.add(new Account());
                        // }
                        return new ResponseEntity<>(res.getBody().getUserInfo().getAccounts(), HttpStatus.OK);
                } catch (Exception e) {
                        createUserResponse response = new createUserResponse("error",
                                        " Can't find User with this phone Number");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

        // Register User
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody Users users) {

                System.out.println(passwordEncoder.encode(users.getPassword()));
                
                if (userRepository.findByUsername(users.getUsername()) != null) {
                        createUserResponse response = new createUserResponse("error", "User already exists!");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }


                ResponseEntity<UserInfoResponse> res;
                try {
                        String uri = "http://10.1.245.150:7081/userInfo";
                        RestTemplate restTemplate = new RestTemplate();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        String requestBody = "{\"phoneNumber\":" + users.getUsername() + "}";

                        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                        res = restTemplate.exchange(uri, HttpMethod.POST, request, UserInfoResponse.class);

                        UserInfo userInfo = res.getBody().getUserInfo();
                        users.setFullName(userInfo.getFullName());
                        users.setAddress(userInfo.getAddress());
                        users.setEmail(userInfo.getEmail());
                        users.setBirthDate(userInfo.getEmail());
                        users.setGender(userInfo.getGender());
                        users.setImageUrl(userInfo.getImageUrl());
                        users.setLanguageCode(Integer.parseInt(userInfo.getLanguageCode()));
                        users.setEmailConfirmed(false);
                        users.setAccessFailedCount(0);
                        users.setIsEnabled(true);
                        users.setCreatedAt(LocalDateTime.now().toString());
                        users.setTwoFactorEnabled(false);
                } catch (Exception e) {
                        createUserResponse response = new createUserResponse("error",
                                        "Can't find User with this phone Number on CBS!");

                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                List<Role> roles = new ArrayList<Role>(1);
                roles.add(this.roleRepo.findByRoleName("user"));
                users.setRoles(roles);
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                users.setAccounts(users.getAccounts());

                return new ResponseEntity<>(userRepository.save(users), HttpStatus.CREATED);
        }

}

@Data
class MobileNumber {
        private String phoneNumber;
}

@Data
class UserInfoResponse {
        private UserInfo userInfo;
}

@Data
class UserInfo {
        private String fullName;
        private String gender;
        private String dateOfBirth;
        private String email;
        private String imageUrl;
        private String languageCode;
        ArrayList<Account> accounts = new ArrayList<Account>();
        Address address;
        @JsonProperty("Status")
        private String Status;
}

