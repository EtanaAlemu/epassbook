package com.dxvalley.epassbook.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.dxvalley.epassbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dxvalley.epassbook.models.Otp;
import com.dxvalley.epassbook.repositories.OtpRepository;
import com.dxvalley.epassbook.services.OtpService;


@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/verify/{otpCode}")
    public ResponseEntity<?> Verify(@PathVariable String otpCode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Otp otp = otpRepository.findOtpByOtpCode(otpCode);

        if(otp == null){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiredAt  = LocalDateTime.parse(otp.getOtpExpireDate(), dateTimeFormatter);
        if (expiredAt.isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            return new ResponseEntity<>(
                    "This otpCode has already expired. Please try again.",
                    HttpStatus.BAD_REQUEST);
        }
        var user = userRepository.findByUsername(otp.getPhoneNumber());
        user.setIsEnabled(true);
        otpRepository.delete(otp);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

}
