package com.dxvalley.epassbook.appConnect;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CBOAccountService {

    final private String accountUrl;

    public CBOAccountService(@Value("${appConnect.accountUrl}") String accountUrl) {
        this.accountUrl = accountUrl;
    }
    public String getAccountBalance(String accountNumber) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String reqBody = "{\"BalanceEnquiryRequest\":{\"ESBHeader\":{\"serviceCode\":\"300000\",\"channel\":\"USSD\",\"Service_name\":\"BalanceEnquiry\",\"Message_Id\":\"6255726662\"},\"WebRequestCommon\":{\"company\":\"ET0010222\",\"password\":\"123456\",\"userName\":\"MMTUSER1\"},\"ACCTBALCTSType\":[{\"columnName\":\"ACCOUNT.NUMBER\",\"criteriaValue\":\"" + accountNumber + "\",\"operand\":\"EQ\"}]}}";

            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(accountUrl, HttpMethod.POST, request, String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("BalanceEnquiryResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();

            if (ESBStatus.getString("status").equals("Success")) {
                String balance = new JSONObject(response.getBody())
                        .getJSONObject("BalanceEnquiryResponse")
                        .getJSONObject("ACCTBALCTSType")
                        .getJSONObject("gACCTBALCTSDetailType")
                        .getJSONObject("mACCTBALCTSDetailType")
                        .getJSONObject("mACCTBALCTSDetailType")
                        .getString("WorkingBal");

                resBody.put("status", "success");
                resBody.put("statement", String.valueOf(balance));

                return balance; //return the balance
            } else {

                // return ESBStatus.getString("errorDescription");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
            // return e.toString();
        }
    }

}
