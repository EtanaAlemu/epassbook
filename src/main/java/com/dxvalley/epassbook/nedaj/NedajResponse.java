package com.dxvalley.epassbook.nedaj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NedajResponse {
    private String agentId;
    private String merchantId;
    private String fuelType;
    private String messageId;
    private String transactionID;
    @JsonProperty("DEBITACCTNO")
    private String DEBITACCTNO;
    @JsonProperty("DEBITAMOUNT")
    private String DEBITAMOUNT;
    @JsonProperty("TRANSACTION_DATE")
    private String TRANSACTION_DATE;
    @JsonProperty("STATUS")
    private String STATUS;
}

