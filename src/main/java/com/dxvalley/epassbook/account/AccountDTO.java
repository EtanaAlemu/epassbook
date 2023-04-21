package com.dxvalley.epassbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private String accountTitle;
    private String coCode;
    private String branchName;
}
