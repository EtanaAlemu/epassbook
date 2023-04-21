package com.dxvalley.epassbook.nedaj;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NedajRequest {
    @NotEmpty
    private String merchantId;
    @NotEmpty
    private String agentId;
    @NotEmpty
    private String fuelType;
    @NotEmpty
    private String debitAccount;
    @NotEmpty
    private String debitAmount;
    private String messageId;

}