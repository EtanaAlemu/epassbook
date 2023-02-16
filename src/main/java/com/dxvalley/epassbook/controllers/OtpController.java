package com.dxvalley.epassbook.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/otp")
public class OtpController {
    private final OtpService otpService;
    private final OtpRepository otpRepository;

    public OtpController(OtpService otpService, OtpRepository otpRepository) {
        this.otpService = otpService;
        this.otpRepository = otpRepository;
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }

    @PostMapping("/send")
    public ResponseEntity<?> getUserInfo(@RequestBody OtpRequest otpRequest) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:s");
        Date currentDate = new Date();
        Date exprireDate = new Date();
        if (this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()) != null) {
            try {
                exprireDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:s")
                        .parse(this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()).getOtpExpireDate());
                System.out.println(dateFormat.format(currentDate));
                System.out.println(dateFormat.format(exprireDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long MAX_DURATION = MILLISECONDS.convert(2, MINUTES);
            long duration = currentDate.getTime() - exprireDate.getTime();
            System.out.println(duration);
            System.out.println(MAX_DURATION);
            System.out.println(duration >= MAX_DURATION);
            if (duration <= MAX_DURATION) {
                otpService.deleteOtp(this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()).getOtpId());
            }
        }
        if (this.otpRepository.findOtpByPhoneNumber(otpRequest.getPhoneNumber()) == null) {
            try {
                String uri = "http://10.1.245.150:7080/v1/otp/";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String code = getRandomNumberString();
                String requestBody = "{\"mobile\":" + "\"" + otpRequest.getPhoneNumber() + "\"," + "\"text\":" + "\""
                        + code + "\"" + "}";
                System.out.println(requestBody);
                HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

                ResponseEntity<OtpResponse> res = restTemplate.exchange(uri, HttpMethod.POST, request,
                        OtpResponse.class);

                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);
                c.add(Calendar.MINUTE, 0);
                Date expirayDate = c.getTime();
                System.out.println(dateFormat.format(currentDate));
                System.out.println(dateFormat.format(expirayDate));

                Otp otp = new Otp();
                otp.setPhoneNumber(otpRequest.getPhoneNumber());
                otp.setOtpCode(code);
                otp.setOtpExpireDate(dateFormat.format(expirayDate));
                otpService.addOtp(otp);
                return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
            } catch (Exception e) {
                OtpException response = new OtpException("error", e.getMessage());
                return new ResponseEntity<OtpException>(response, HttpStatus.NOT_FOUND);
            }
        } else {

            OtpException response = new OtpException("error", "otp already sent");
            return new ResponseEntity<OtpException>(response, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/verify/{phoneNumber}")
    public ResponseEntity<VerifyResponse2> Verify(@RequestBody VerifyRequest request,
            @PathVariable String phoneNumber) {
        Otp otp = otpRepository.findOtpByPhoneNumber(phoneNumber);
        if (otp != null) {
            if (otp.getOtpCode().equals(request.getCode())) {
                VerifyResponse2 response = new VerifyResponse2("Verfied");
                return new ResponseEntity<VerifyResponse2>(response, HttpStatus.OK);
            } else {
                VerifyResponse2 response = new VerifyResponse2("Incorrect code");
                return new ResponseEntity<VerifyResponse2>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            VerifyResponse2 response = new VerifyResponse2("Request for code first");
            return new ResponseEntity<VerifyResponse2>(response, HttpStatus.BAD_REQUEST);
        }
    }

}


@Getter
@Setter
@AllArgsConstructor
class VerifyRequest {
    private String code;
}

@Getter
@Setter
@AllArgsConstructor
class VerifyResponse2 {
    private String status;
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