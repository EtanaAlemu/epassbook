package com.dxvalley.epassbook.dto;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.models.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UserInfo {
        private String username;
        private String password;
        private String phoneNumber;
        private String fullName;
        private String gender;
        private String dateOfBirth;
        private String email;
        private String imageUrl;
        private String languageCode;
        ArrayList<Account> accounts = new ArrayList<Account>();
        Address address;
        @JsonProperty("Status")
        private String Status;
    }
