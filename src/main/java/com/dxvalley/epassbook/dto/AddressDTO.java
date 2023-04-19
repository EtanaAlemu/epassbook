package com.dxvalley.epassbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String country;
    private String city;
    private String subCity;
    private String woreda;
    private String kebele;
    private String houseNumber;
}