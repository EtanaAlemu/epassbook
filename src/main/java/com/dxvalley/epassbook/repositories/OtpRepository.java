package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dxvalley.epassbook.models.Otp;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findOtpByPhoneNumber(String phoneNumber);
    Otp findOtpByOtpCode(String otpCode);
}
