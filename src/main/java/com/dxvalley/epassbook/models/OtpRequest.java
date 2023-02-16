package com.dxvalley.epassbook.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpRequest {
    private String phoneNumber;
}
