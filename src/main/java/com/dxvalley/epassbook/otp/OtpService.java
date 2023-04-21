package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.models.Otp;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    Otp addOTP(String phoneNumber, String optCode);
    boolean verifyOTP(String otpCode);
    ResponseEntity sendOTP(String phoneNumber);
    ResponseEntity sendOtpByUsername(String username);
}
