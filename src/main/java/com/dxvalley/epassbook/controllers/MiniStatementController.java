package com.dxvalley.epassbook.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
//import org.json.simple.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ministatement")
public class MiniStatementController {

    @PostMapping()
    public ResponseEntity<?> miniStatement(@RequestBody MiniStatement req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));
            String reqBody = "{\n" +
                    "    \"MiniStatementRequest\": {\n" +
                    "        \"ESBHeader\": {\n" +
                    "            \"serviceCode\": \"100000\",\n" +
                    "            \"channel\": \"USSD\",\n" +
                    "            \"Service_name\": \"MiniStatement\",\n" +
                    "            \"Message_Id\": \"EPAS"+messageId+"\"\n" +
                    "        },\n" +
                    "        \"EMMTMINISTMTType\": [\n" +
                    "            {\n" +
                    "                \"columnName\": \"ACCOUNT\",\n" +
                    "                \"criteriaValue\": \""+req.getAccountNumber()+"\",\n" +
                    "                \"operand\": \"EQ\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";

            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("MiniStatementResponse")
                    .getJSONObject("ESBStatus");
            Map<String, Object> resBody = new HashMap();


            if (ESBStatus.getString("status").equals("Success")) {
                JSONArray MINISTMT = new JSONObject(response.getBody())
                        .getJSONObject("MiniStatementResponse")
                        .getJSONObject("EMMTMINISTMTType")
                        .getJSONObject("gEMMTMINISTMTDetailType")
                        .getJSONArray("mEMMTMINISTMTDetailType");

                resBody.put("status", "success");
//                JSONParser parser = new JSONParser();
                ArrayList<Statement> statements = new ArrayList<>();
                for(int i=0; i < MINISTMT.length(); i++)
                {
//                    JSONObject json = (JSONObject) parser.parse(String.valueOf(MINISTMT.getJSONObject(i)));
                    JSONObject json = MINISTMT.getJSONObject(i);
                    Statement s = new Statement();

                    s.TXNREF = json.getString("TXNREF");
                    s.CRAMT = json.getString("CRAMT");
                    s.DRAMT = json.getString("DRAMT");
                    s.DATE = json.getString("DATE");
                    s.DESC = json.getString("DESC");
                    if(json.getJSONObject("BALANCE").isEmpty())
                    s.BALANCE = "";
                    else
                    s.BALANCE = json.getString("BALANCE");
                    statements.add(s);
                }

                resBody.put("statement", statements);
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            } else {

                JSONArray errorDescription = new JSONArray(ESBStatus.getJSONArray("errorDescription"));

                resBody.put("status", "failure");
                resBody.put("message", errorDescription.get(0).toString());
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class MiniStatement{

    String accountNumber;

}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Statement{

    String TXNREF;
    String CRAMT;
    String DRAMT;
    String DATE;
    String DESC;
    String BALANCE;

}
