package com.dxvalley.epassbook.user;

import com.dxvalley.epassbook.utils.ApiResponse;
import com.dxvalley.epassbook.utils.ChangePinDTO;
import com.dxvalley.epassbook.exceptions.ResourceAlreadyExistsException;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.user.address.Address;
import com.dxvalley.epassbook.account.AccountService;
import com.dxvalley.epassbook.user.address.AddressService;
import com.dxvalley.epassbook.otp.OtpService;
import com.dxvalley.epassbook.appConnect.CBOUserService;
import com.dxvalley.epassbook.user.role.Role;
import com.dxvalley.epassbook.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AddressService addressService;
    @Autowired
    AccountService accountService;
    @Autowired
    CBOUserService cboUserService;
    @Autowired
    OtpService otpService;

    @Override
    public ResponseEntity checkUserExistenceAndSendOTP(String phoneNumber) {
        var user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent())
            throw new ResourceAlreadyExistsException("This phone number is already used on this platform!");

        var userInfo = cboUserService.getUserInfo(phoneNumber);
        otpService.sendOTP(phoneNumber);
        return ApiResponse.success(userInfo);
    }
    @Override
    public ResponseEntity registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new ResourceAlreadyExistsException("There is already a user with this username.");

        Users users = new Users();
        List<Role> roles = new ArrayList<Role>(1);

        roles.add(roleRepository.findByRoleName("user"));
        users.setUsername(userDTO.getUsername());
        users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        users.setRoles(roles);
        users.setFullName(userDTO.getFullName());
        users.setEmail(userDTO.getEmail());
        users.setEmailConfirmed(false);
        users.setDateOfBirth(userDTO.getDateOfBirth());
        users.setGender(userDTO.getGender());
        users.setImageUrl(userDTO.getImageUrl());
        users.setCreatedAt(LocalDateTime.now().toString());
        users.setAccessFailedCount(0);
        users.setTwoFactorEnabled(false);
        users.setIsEnabled(true);
        users.setPhoneNumber(userDTO.getPhoneNumber());
        users.setLanguageCode(0);

        Address address = addressService.saveAddress(userDTO.getAddress());
        users.setAddress(address);

        var accountList = accountService.saveAccounts(userDTO.getAccounts());
        users.setAccounts(accountList);
        userRepository.save(users);

        return ApiResponse.created("Registered successfully.");
    }

    @Override
    public ResponseEntity getUserByUsername(String username) {
        Users user = utilGetUserByUsername(username);
        user.setPassword(null);
        return ApiResponse.success(user);
    }

    @Override
    public Users utilGetUserByUsername(String username) {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundException("There is no user with this username."));
    }

    @Override
    public ResponseEntity changePin(String username, ChangePinDTO changePinDTO) {
        Users user = utilGetUserByUsername(username);

        if(changePinDTO.getOldPassword().equals(changePinDTO.getNewPassword()))
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "You can't reuse your old password again.");

        Boolean isPasswordMatch = passwordEncoder.matches(changePinDTO.getOldPassword(), user.getPassword());
        if (!isPasswordMatch)
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Incorrect old password.");

        if (!areAllDigitsUnique(changePinDTO.getNewPassword()))
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "All four digits must be unique.");

        user.setPassword(passwordEncoder.encode(changePinDTO.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success("Your password has been changed successfully.");
    }
    private boolean areAllDigitsUnique (String str){
        Set<Character> seen = new HashSet<>();
        for (char c : str.toCharArray()) {
            if (seen.contains(c)) {
                return false;
            }
            seen.add(c);
        }
        return true;
    }
}