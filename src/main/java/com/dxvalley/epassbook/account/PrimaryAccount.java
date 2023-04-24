package com.dxvalley.epassbook.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrimaryAccount {
    String accountNumber;
    @NotNull
    Integer passcode;
    @NotEmpty
    String phoneNumber;
}
