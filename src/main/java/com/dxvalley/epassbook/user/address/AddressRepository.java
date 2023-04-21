package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dxvalley.epassbook.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByAddressId(Long addressId);
}
