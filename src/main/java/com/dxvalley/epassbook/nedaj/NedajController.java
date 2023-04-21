package com.dxvalley.epassbook.nedaj;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nedaj")
@RequiredArgsConstructor
public class NedajController {
    private final NedajService nedajService;

    @PostMapping("/{username}")
    ResponseEntity<?> nedaj(@PathVariable String username, @RequestBody @Validated NedajRequest nedajRequest) {
        return nedajService.handleNedajRequest(username, nedajRequest);
    }
}