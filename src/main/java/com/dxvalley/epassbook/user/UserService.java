package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.dto.ChangePinDTO;
import com.dxvalley.epassbook.dto.UserDTO;
import com.dxvalley.epassbook.dto.UserInfoDTO;
import com.dxvalley.epassbook.models.Users;
import org.springframework.http.ResponseEntity;

public interface UserService {
    Users registerUser(UserDTO userDTO);
    ResponseEntity changePin(String username, ChangePinDTO changePinDTO);
    Users getUserByUsername(String username);
    Users getUserByPhoneNumber(String phoneNumber);
    ResponseEntity checkUserExistenceAndSendOTP(String phoneNumber);
    Users getUserById(Long userId);
}
