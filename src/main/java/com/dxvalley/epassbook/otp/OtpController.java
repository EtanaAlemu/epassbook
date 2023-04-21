package com.dxvalley.epassbook.controllers;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.services.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    OtpService otpService;

    @PutMapping("/verify/{otpCode}")
    public ResponseEntity<?> verifyOtp(@PathVariable String otpCode) {
        if (otpService.verifyOTP(otpCode))
            return ApiResponse.success("Verified successfully");

        return ApiResponse.error(HttpStatus.BAD_REQUEST, "This OTP has already expired. Please try again.");
    }

    @PostMapping("/sendOTP/{username}")
    public ResponseEntity<?> sendOTP(@PathVariable String username) {
        return otpService.sendOtpByUsername(username);
    }
}
