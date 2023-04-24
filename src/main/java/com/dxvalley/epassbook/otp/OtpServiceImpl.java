package com.dxvalley.epassbook.otp;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String OTP_URI = "http://10.1.245.150:7081/v1/otp/";
    @Override
    public Otp addOTP(String phoneNumber, String optCode) {
        Otp otp = new Otp();
        otp.setOtpCode(optCode);
        otp.setPhoneNumber(phoneNumber);
        otp.setOtpExpireDate(LocalDateTime.now().plusMinutes(3).format(dateTimeFormatter));
        return otpRepository.save(otp);
    }

    @Override
    public ResponseEntity sendOtpByUsername(String username) {
        var user = userRepository.findByPhoneNumber((username)).orElseThrow(
                () -> new ResourceNotFoundException("There is no user with this username"));
        return sendOTP(user.getPhoneNumber());
    }
    @Override
    public ResponseEntity sendOTP(String phoneNumber) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String code = String.format("%05d", new Random().nextInt(99999));
            String requestBody = "{\"mobile\":" + "\"" + phoneNumber + "\"," + "\"text\":" + "\""
                    + code + "\"" + "}";

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    OTP_URI, HttpMethod.POST, request, String.class);

            addOTP(phoneNumber, code);
            return ApiResponse.success("An OTP with five digits is sent to your phone. Please check it");
        } catch (HttpClientErrorException e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Sorry, SMS service is down for a moment. Please try again later.");
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Sorry, something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseEntity verifyOTP(String otpCode) {
        Otp otp = otpRepository.findOtpByOtpCode(otpCode).
                orElseThrow(() -> new ResourceNotFoundException("Invalid OTP code"));

        LocalDateTime expiredAt  = LocalDateTime.parse(otp.getOtpExpireDate(), dateTimeFormatter);
        otpRepository.delete(otp);
        if (expiredAt.isBefore(LocalDateTime.now()))
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "This OTP has already expired. Please try again.");

        return ApiResponse.success("Verified successfully");
    }
}
