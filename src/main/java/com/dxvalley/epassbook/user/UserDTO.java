package com.dxvalley.epassbook.user;

import com.dxvalley.epassbook.account.dto.AccountDTO;
import com.dxvalley.epassbook.user.address.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String fullName;
    private String phoneNumber;
    private String username;
    private String password;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String imageUrl;
    private String languageCode;
    private List<AccountDTO> accounts;
    private AddressDTO address;
}


