package com.dxvalley.epassbook.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.dxvalley.epassbook.dto.CampaignDTO;
import com.dxvalley.epassbook.dto.CampaignListDTO;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.json.*;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @GetMapping
    public ResponseEntity<?> getCampaigns(){

        try {
            String url = "http://10.1.177.121:8181/api/campaigns/getCampaignsByStage/funding";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);

            // Object[] objects = response.getBody();
            // ObjectMapper mapper = new ObjectMapper();
            
            // List<CampaignDTO> campaigns = Arrays.stream(objects)
            //                         .map(object -> mapper.convertValue(object, CampaignDTO.class))
            //                         .map(CampaignDTO::getTitle)
            //                         .collect(Collectors.toList());

            return new ResponseEntity<>(response.getBody(),  HttpStatus.OK);

        } catch (Exception e) {
            // e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),  HttpStatus.OK);
        }
    }
}


