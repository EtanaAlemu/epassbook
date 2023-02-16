package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.models.Otp;

public interface OtpService {
    Otp addOtp (Otp otp);
    void deleteOtp( Long otpId);
}
