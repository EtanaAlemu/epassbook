package com.dxvalley.epassbook.user.address;

import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity updateAddress(String username, AddressDTO addressDTO);

    Address saveAddress(AddressDTO addressDTO);

}
