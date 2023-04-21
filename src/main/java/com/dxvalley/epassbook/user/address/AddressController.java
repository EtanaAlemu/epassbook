package com.dxvalley.epassbook.user.address;

import lombok.RequiredArgsConstructor;;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;
    @PutMapping("/{username}")
    ResponseEntity updateAddress(@PathVariable String username, @RequestBody AddressDTO addressDTO){
        return addressService.updateAddress(username, addressDTO);
    }
}
