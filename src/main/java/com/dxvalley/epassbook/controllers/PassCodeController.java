package com.dxvalley.epassbook.controllers;

import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.models.Passcode;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.PasscodeRepository;
import com.dxvalley.epassbook.repositories.RoleRepository;
import com.dxvalley.epassbook.repositories.UserRepository;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/passcode")
public class PassCodeController {

    private final UserRepository userRepository;
    private final PasscodeRepository passcodeRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId) {
        var user = userRepository.findByUserId(userId);
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "Cannot find this user!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(userId);
        if (passcode == null) {
            createUserResponse response = new createUserResponse("error", "Cannot find passcode!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(passcode, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "Cannot find this user!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(user.getUserId());
        if (passcode == null) {
            createUserResponse response = new createUserResponse("error", "Cannot find passcode!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(passcode, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createPasscode( @RequestBody Passcode req) {
        var user = userRepository.findByUserId(req.getUserId());
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "user doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(req.getUserId());
        if (passcode != null) {
            createUserResponse response = new createUserResponse("error", "passcode already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        passcodeRepository.save(req);
        createUserResponse response = new createUserResponse("success", "passcode created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validatePasscode( @RequestBody Validate req) {
        var user = userRepository.findByUsername(req.getUsername());
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "user doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(user.getUserId());
        if (passcode == null) {
            createUserResponse response = new createUserResponse("error", "passcode doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(passcode.getPasscode(), req.getPasscode())) {
            createUserResponse response = new createUserResponse("error", "passcode doesn't match");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        createUserResponse response = new createUserResponse("success", "passcode match");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updatePasscode(@RequestBody Passcode req) {
        var user = userRepository.findByUserId(req.getUserId());
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "user doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(req.getUserId());
        if (passcode == null) {
            createUserResponse response = new createUserResponse("error", "passcode doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        passcode.setPasscode(req.getPasscode());
        passcodeRepository.save(passcode);
        createUserResponse response = new createUserResponse("success", "passcode updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deletePasscode(@PathVariable Long userId) {
        var user = userRepository.findByUserId(userId);
        if (user == null) {
            createUserResponse response = new createUserResponse("error", "user doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var passcode = passcodeRepository.findByUserId(userId);
        if (passcode == null) {
            createUserResponse response = new createUserResponse("error", "passcode doesn't exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        passcodeRepository.deleteById(passcode.getId());
        createUserResponse response = new createUserResponse("success", "passcode deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Validate {
    String passcode;
    String username;
}
