package com.dxvalley.epassbook.otp;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findOtpByPhoneNumber(String phoneNumber);

    Optional<Otp> findOtpByOtpCode(String otpCode);
}
