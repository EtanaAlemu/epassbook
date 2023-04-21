package com.dxvalley.epassbook.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePinDTO {
    private String oldPassword;
    private String newPassword;
}
