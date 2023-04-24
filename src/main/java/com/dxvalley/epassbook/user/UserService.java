package com.dxvalley.epassbook.user;

import com.dxvalley.epassbook.utils.ChangePinDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity registerUser(UserDTO userDTO);

    ResponseEntity changePin(String username, ChangePinDTO changePinDTO);

    ResponseEntity getUserByUsername(String username);

    Users utilGetUserByUsername(String username);

    ResponseEntity checkUserExistenceAndSendOTP(String phoneNumber);
}
