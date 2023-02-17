package com.dxvalley.epassbook.notification;

import com.dxvalley.epassbook.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface OtpService {
    ApiResponse sendOtp(String phoneNumber, String randomNumber);
}
