package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dxvalley.epassbook.models.Otp;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findOtpByPhoneNumber(String phoneNumber);
    Optional<Otp> findOtpByOtpCode(String otpCode);
}
