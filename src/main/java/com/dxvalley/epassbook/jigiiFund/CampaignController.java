package com.dxvalley.epassbook.jigiiFund;

import com.dxvalley.epassbook.utils.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @GetMapping
    public ResponseEntity<?> getCampaigns() {

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

            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);

        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.EXPECTATION_FAILED, "Connection refused to crowdfunding server.");
        }
    }
}


