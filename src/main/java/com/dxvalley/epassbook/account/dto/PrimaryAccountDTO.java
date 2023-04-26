package com.dxvalley.epassbook.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrimaryAccountDTO {
    private String accountNumber;
    @NotNull
    private Integer passcode;
    @NotEmpty
    private String phoneNumber;

    public PrimaryAccountDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
