package com.dxvalley.epassbook.user;

import com.dxvalley.epassbook.account.Account;
import com.dxvalley.epassbook.utils.ApiResponse;
import com.dxvalley.epassbook.exceptions.ResourceNotFoundException;
import com.dxvalley.epassbook.utils.ChangePinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/checkUserExistence/{phoneNumber}")
    public ResponseEntity<?> checkUserExistenceAndSendOTP(@PathVariable String phoneNumber) {
        return userService.checkUserExistenceAndSendOTP(phoneNumber);
    }

    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ApiResponse.success("Registered Successfully!");
    }

    @GetMapping("/getUsers")
    List<Users> getUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/getUserByPhone/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        var user = userRepository.findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find user with this phone number!"));


        List<Account> originalAccounts = user.getAccounts();
        Account mainAccount = null;

        for (Account account : originalAccounts) {
            if (account.getIsMainAccount().equals(true)) {
                mainAccount = new Account(account.getAccountNumber());
            }
        }

        List<Account> mainAccountAsList = new ArrayList<>();

        mainAccountAsList.add(mainAccount);

        user.setAccounts(mainAccountAsList);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/changePin/{username}")
    public ResponseEntity<?> changePin(@PathVariable String username, @RequestBody ChangePinDTO changePinDTO) {
        return userService.changePin(username, changePinDTO);
    }
}