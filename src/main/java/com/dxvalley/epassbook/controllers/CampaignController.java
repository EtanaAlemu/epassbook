package com.dxvalley.epassbook.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.dxvalley.epassbook.dto.CampaignDTO;
import com.dxvalley.epassbook.dto.CampaignListDTO;

import org.json.*;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @GetMapping
    public ResponseEntity<?> getCampaigns(){

        try {
            String url = "http://10.1.177.121:8181/api/campaigns/getCampaigns";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);

            return new ResponseEntity<>(response.getBody(),  HttpStatus.OK);

        } catch (Exception e) {
            // e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),  HttpStatus.OK);
        }
    }
}


