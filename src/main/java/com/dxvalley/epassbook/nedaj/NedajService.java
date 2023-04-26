package com.dxvalley.epassbook.nedaj;

import com.dxvalley.epassbook.exceptions.AppConnectException;
import com.dxvalley.epassbook.exceptions.ResourceAlreadyExistsException;
import com.dxvalley.epassbook.user.UserService;
import com.dxvalley.epassbook.user.Users;
import com.dxvalley.epassbook.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NedajService {
    private final NedajRepository nedajRepository;
    private final UserService userService;
    private final DateTimeFormatter dateTimeFormatter;

    @Transactional
    public ResponseEntity<?> handleNedajRequest(String username, NedajRequest nedajRequest) {
        checkUniquenessOFMessageId(nedajRequest.getMessageId());

        Users user = userService.utilGetUserByUsername(username);
        Nedaj nedaj = new Nedaj();

        nedaj.setUser(user);
        nedaj.setAgentId(nedajRequest.getAgentId());
        nedaj.setMerchantId(nedajRequest.getMerchantId());
        nedaj.setFuelType(nedajRequest.getFuelType());
        nedaj.setMessageId(nedajRequest.getMessageId());
        nedaj.setDebitAcctNo(nedajRequest.getDebitAccount());
        nedaj.setDebitAmount(nedajRequest.getDebitAmount());
        nedaj.setTransactionOrderedDate(LocalDateTime.now().format(dateTimeFormatter));

        log.error("Nedaj Payment Request => {}", nedaj);
        try {
            NedajResponse gateWayResponse = sendToGateWay(nedajRequest);

            nedaj.setTransactionID(gateWayResponse.getTransactionID());
            nedaj.setTransactionCompletedDate(gateWayResponse.getTRANSACTION_DATE());
            nedaj.setPaymentStatus(gateWayResponse.getSTATUS().toUpperCase());
            var succeedNedajPayment = nedajRepository.save(nedaj);

            log.error("Succeed Nedaj Payment => {}", succeedNedajPayment);
            return ApiResponse.success("Your account is debited " + gateWayResponse.getDEBITAMOUNT() + " birr for fuel payment");
        } catch (Exception ex) {
            nedaj.setPaymentStatus("FAILED");
            var failedNedajPayment = nedajRepository.save(nedaj);

            log.error("Failed nedaj Payment => {}", failedNedajPayment);
            log.error(ex.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Currently, we cannot process Nedaj payments. Please try again later. Thank you.");
        }
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

            throw new Exception();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppConnectException();
        }
    }

    private void checkUniquenessOFMessageId(String messageId) {
        if (nedajRepository.findByMessageId(messageId).isPresent())
            throw new ResourceAlreadyExistsException("A transaction already exists.");
    }

}

