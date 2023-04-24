package com.dxvalley.epassbook.user.address;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String country;
    private String city;
    private String subCity;
    private String woreda;
    private String kebele;
    private String houseNumber;

    public Address(String country, String city, String subCity, String woreda, String kebele, String houseNumber) {
        this.country = country;
        this.city = city;
        this.subCity = subCity;
        this.woreda = woreda;
        this.kebele = kebele;
        this.houseNumber = houseNumber;
    }
}
