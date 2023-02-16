package com.dxvalley.epassbook.serviceImpl;

import org.springframework.stereotype.Service;

import com.dxvalley.epassbook.models.Otp;
import com.dxvalley.epassbook.repositories.OtpRepository;
import com.dxvalley.epassbook.services.OtpService;

@Service
public class OtpServiceImpl implements OtpService{
    private final OtpRepository otpRepository;

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }
    @Override
    public Otp addOtp(Otp otp) {
        return this.otpRepository.save(otp);
    }

    @Override
    public void deleteOtp(Long otpId) {
        otpRepository.deleteById(otpId);
    }
    
}
