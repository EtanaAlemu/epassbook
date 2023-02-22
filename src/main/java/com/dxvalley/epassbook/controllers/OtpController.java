package com.dxvalley.epassbook.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.dto.UserInfo;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.UserRepository;
import com.dxvalley.epassbook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dxvalley.epassbook.models.Otp;
import com.dxvalley.epassbook.repositories.OtpRepository;
import com.dxvalley.epassbook.services.OtpService;


@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private UserService userService;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/verify/{otpCode}")
    public ResponseEntity<?> OtpVerify(@PathVariable String otpCode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Otp otp = otpRepository.findOtpByOtpCode(otpCode);

        if(otp == null){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiredAt  = LocalDateTime.parse(otp.getOtpExpireDate(), dateTimeFormatter);
        if (expiredAt.isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            return new ResponseEntity<>(
                    "This otpCode has already expired. Please try again.",
                    HttpStatus.BAD_REQUEST);
        }
        otpRepository.delete(otp);
        var response =  new ApiResponse("Success","Verified successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
