package com.dxvalley.epassbook.serviceImpl;

import com.dxvalley.epassbook.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.dxvalley.epassbook.models.Otp;
import com.dxvalley.epassbook.repositories.OtpRepository;
import com.dxvalley.epassbook.services.OtpService;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService{
    @Autowired
    private OtpRepository otpRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public Otp addOtp(String phoneNumber, String optCode) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);
        Otp otp = new Otp();
        otp.setOtpCode(optCode);
        otp.setPhoneNumber(phoneNumber);
        otp.setOtpExpireDate(expiredAt.format(dateTimeFormatter));
        return this.otpRepository.save(otp);
    }

    @Override
    public ApiResponse sendOtp(String phoneNumber) {

        ResponseEntity<String> res;
        try {
            String uri = "http://10.1.245.150:7080/v1/otp/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String code = getRandomNumberString();
            String requestBody = "{\"mobile\":" + "\"" + phoneNumber + "\"," + "\"text\":" + "\""
                    + code + "\"" + "}";

            HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
            res = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

            if (res.getStatusCode().toString().trim().equals("200 OK")) {
                addOtp(phoneNumber,code);
                return new ApiResponse(
                        "success",
                        "Message sent to your phone number.");

            } else {
                return new ApiResponse(
                        "bad request",
                        "Please inter valid Mobile number!");
            }
        } catch (Exception e) {
            return new ApiResponse(
                    "error",
                    "Sorry, SMS service is down for a moment. Please try again later.");

        }
    }

    @Override
    public void deleteOtp(Long otpId) {
        otpRepository.deleteById(otpId);
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }

}
