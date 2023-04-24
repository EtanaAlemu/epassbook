package com.dxvalley.epassbook.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    OtpService otpService;

    @PutMapping("/verify/{otpCode}")
    public ResponseEntity<?> verifyOtp(@PathVariable String otpCode) {
        return otpService.verifyOTP(otpCode);
    }

    @PostMapping("/sendOTP/{username}")
    public ResponseEntity<?> sendOTP(@PathVariable String username) {
        return otpService.sendOtpByUsername(username);
    }
}
