package com.dxvalley.epassbook.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.dxvalley.epassbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.dxvalley.epassbook.models.Otp;
import com.dxvalley.epassbook.models.OtpRequest;
import com.dxvalley.epassbook.repositories.OtpRepository;
import com.dxvalley.epassbook.services.OtpService;

import static java.util.concurrent.TimeUnit.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/verify/{otp}")
    public ResponseEntity<?> Verify(@PathVariable String otpCode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Otp otp = otpRepository.findOtpByOtpCode(otpCode);

        if(otp == null){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime expiredAt  = LocalDateTime.parse(otp.getOtpExpireDate(), dateTimeFormatter);
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(
                    "This otpCode has already expired. Please try again.",
                    HttpStatus.BAD_REQUEST);
        }
        var user = userRepository.findByUsername(otp.getPhoneNumber());
        user.setIsEnabled(true);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

}

@Data
class OtpResponse {
    private String Response;
    private String status;
    private String responseCode;
}

@Getter
@Setter
@AllArgsConstructor
class OtpException {
    private String state;
    private String message;
}