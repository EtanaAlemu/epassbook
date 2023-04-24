package com.dxvalley.epassbook.user.address;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByAddressId(Long addressId);
}
