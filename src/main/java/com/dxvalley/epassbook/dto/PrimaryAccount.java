package com.dxvalley.epassbook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class PrimaryAccount {
    String accountNumber;
    @NotNull
    Integer passcode;
    @NotEmpty
    String phoneNumber;
}
