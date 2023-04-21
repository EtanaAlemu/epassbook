package com.dxvalley.epassbook.appConnect;

import com.dxvalley.epassbook.utils.UserInfoDTO;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CBOUserService {

    final private String userUrl;

    public CBOUserService(@Value("${appConnect.userUrl}") String userUrl) {
        this.userUrl = userUrl;
    }
    public Object getUserInfo(String phoneNumber) {
        ResponseEntity<String> res;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{\"phoneNumber\":\"" + phoneNumber + "\"}";
            HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
            res = restTemplate.exchange(userUrl, HttpMethod.POST, request, String.class);

            if(res.getStatusCode() == HttpStatus.OK){
                ObjectMapper objectMapper = new ObjectMapper();
                UserInfoDTO userInfoData = objectMapper.readValue(res.getBody(), UserInfoDTO.class);
                userInfoData.getUserInfo().setPhoneNumber(phoneNumber);
                return userInfoData.getUserInfo();
            }
            else throw new ResourceNotFoundException("nOT FOUND");

        } catch (Exception e) {
            throw new ResourceNotFoundException("Can't process the request currently. Please try again later!");
        }
    }
}

