package com.dxvalley.epassbook.otp;


import org.springframework.http.ResponseEntity;

public interface OtpService {
    Otp addOTP(String phoneNumber, String optCode);

    ResponseEntity verifyOTP(String otpCode);

    ResponseEntity sendOTP(String phoneNumber);
    ResponseEntity sendOtpByUsername(String username);
}
