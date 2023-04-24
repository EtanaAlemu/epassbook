package com.dxvalley.epassbook.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apiStatus")
public class APIStatusController {
    @Autowired
    APIStatusService apiStatusService;

    @GetMapping
    ResponseEntity<?> getApiStatus() {
        return new ResponseEntity<>(apiStatusService.getApiStatus(), HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> updateApiStatus(@RequestBody APIStatus apiStatus) {
        return new ResponseEntity<>(apiStatusService.updateApiStatus(apiStatus), HttpStatus.OK);
    }
}