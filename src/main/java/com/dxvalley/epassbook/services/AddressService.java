package com.dxvalley.epassbook.services;

import org.springframework.stereotype.Service;

import com.dxvalley.epassbook.models.Address;
import com.dxvalley.epassbook.repositories.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final AddressRepository addressRepository;

  public void editAddress(Address oldAddress, Address newAddress) {
    Address address = addressRepository.findByAddressId(oldAddress.getAddressId());
    address.setEmail(newAddress.getEmail());
    address.setPhoneNumber(newAddress.getPhoneNumber());
    address.setCountry(newAddress.getCountry());
    address.setCity(newAddress.getCity());
    address.setSubCity(newAddress.getSubCity());
    address.setWoreda(newAddress.getWoreda());
    address.setHouseNumber(newAddress.getHouseNumber());
  }
}
