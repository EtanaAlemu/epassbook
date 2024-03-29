package com.dxvalley.epassbook.otp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Otp {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long otpId;
    private String phoneNumber;
    private String otpCode;
    private String otpExpireDate;
}
