package com.dxvalley.epassbook.nedaj;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.AppConnectException;
import com.dxvalley.epassbook.exceptions.ResourceAlreadyExistsException;
import com.dxvalley.epassbook.user.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class NedajService {
    private final NedajRepository nedajRepository;
    private final UserService userService;

    public ResponseEntity<?> handleNedajRequest(String username, NedajRequest nedajRequest) {
        userService.utilGetUserByUsername(username);
        NedajResponse gateWayResponse = sendToGateWay(nedajRequest);
        saveNedaj(username, gateWayResponse);
        return ApiResponse.success("Your account is debited " + gateWayResponse.getDEBITAMOUNT() + " birr for fuel payment");
    }


    public NedajResponse sendToGateWay(NedajRequest nedajRequest) {
        try {
            final String URI = "http://192.168.14.43:5000/api/agent/payment";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestBody = new JSONObject();
            requestBody.put("merchantId", nedajRequest.getMerchantId());
            requestBody.put("agentId", nedajRequest.getAgentId());
            requestBody.put("fuelType", nedajRequest.getFuelType());
            requestBody.put("debitAccount", nedajRequest.getDebitAccount());
            requestBody.put("debitAmount", nedajRequest.getDebitAmount());
            requestBody.put("messageId", nedajRequest.getMessageId());

            HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);
            ResponseEntity<NedajResponse> response = restTemplate.postForEntity(URI, request, NedajResponse.class);

            if (response.getStatusCode() == HttpStatus.OK)
                return response.getBody();

            throw new ResourceAlreadyExistsException("A transaction already exists.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AppConnectException();
        }
    }

    private void saveNedaj(String username, NedajResponse nedajResponse) {
        Nedaj nedaj = new Nedaj();

        nedaj.setAgentId(nedajResponse.getAgentId());
        nedaj.setMerchantId(nedajResponse.getMerchantId());
        nedaj.setFuelType(nedajResponse.getFuelType());
        nedaj.setMessageId(nedajResponse.getMessageId());
        nedaj.setTransactionID(nedajResponse.getTransactionID());
        nedaj.setDebitAcctNo(nedajResponse.getDEBITACCTNO());
        nedaj.setDebitAmount(nedajResponse.getDEBITAMOUNT());
        nedaj.setTransactionDate(nedajResponse.getTRANSACTION_DATE());

        nedaj.setUser(userService.utilGetUserByUsername(username));

        nedajRepository.save(nedaj);
    }

    private String generateUniqueMessageId(String fuelType) {
        final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        final int LENGTH = 15;
        SecureRandom random = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(ALL_CHARS.length());
            stringBuilder.append(ALL_CHARS.charAt(randomIndex));
        }
        String randomString = stringBuilder.toString();
        String first3Letters = fuelType.substring(0, 3).toUpperCase();

        return first3Letters + randomString;
    }

}

