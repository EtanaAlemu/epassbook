package com.dxvalley.epassbook.user.address;

import com.dxvalley.epassbook.dto.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity updateAddress(String username, AddressDTO addressDTO) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = user.getAddress();
        address.setCountry(addressDTO.getCountry() != null ? addressDTO.getCountry() : address.getCountry());
        address.setCity(addressDTO.getCity() != null ? addressDTO.getCity() : address.getCity());
        address.setSubCity(addressDTO.getSubCity() != null ? addressDTO.getSubCity() : address.getSubCity());
        address.setWoreda(addressDTO.getWoreda() != null ? addressDTO.getWoreda() : address.getWoreda());
        address.setKebele(addressDTO.getKebele() != null ? addressDTO.getKebele() : address.getKebele());
        address.setHouseNumber(addressDTO.getHouseNumber() != null ? addressDTO.getHouseNumber() : address.getHouseNumber());

        return ApiResponse.success(addressRepository.save(address));
    }

    @Override
    public Address saveAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setCountry(addressDTO.getCountry());
        address.setCity(addressDTO.getCity());
        address.setSubCity(addressDTO.getSubCity());
        address.setWoreda(addressDTO.getWoreda());
        address.setKebele(addressDTO.getKebele());
        address.setHouseNumber(addressDTO.getHouseNumber());
        return addressRepository.save(address);
    }
}

