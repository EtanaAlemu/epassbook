package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.dto.AddressDTO;
import com.dxvalley.epassbook.models.Address;

public interface AddressService {
  void editAddress(Address oldAddress, Address newAddress);
  Address saveAddress(AddressDTO addressDTO);

}
