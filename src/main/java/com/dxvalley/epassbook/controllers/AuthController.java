package com.dxvalley.epassbook.controllers;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.dto.UserInfo;;
import com.dxvalley.epassbook.services.OtpService;
import com.dxvalley.epassbook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.UserRepository;

import lombok.Data;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
       @Autowired
       private UserRepository userRepository;
       @Autowired
       private OtpService otpService;
       @Autowired
       private UserService userService;

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
                        ApiResponse response = new ApiResponse(
                                "conflict",
                                "This phoneNumber is already used on this platform!");
                        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
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
                otpService.sendOtp(phoneNumber.getPhoneNumber());
                return new ResponseEntity<>(res.getBody().getUserInfo(), HttpStatus.OK);
        }

        @PutMapping("/register")
        public ResponseEntity<?> accept(@RequestBody Users tempUser) {
            Users user = userService.registerUser(tempUser);
            return new ResponseEntity<>("registered Successfully", HttpStatus.OK);
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

}

@Data
class MobileNumber {
        private String phoneNumber;
}

@Data
class UserInfoResponse {
        private UserInfo userInfo;
}


