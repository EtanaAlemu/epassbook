package com.dxvalley.epassbook.controllers;

import com.dxvalley.epassbook.models.User;
import com.dxvalley.epassbook.repository.RoleRepository;
import com.dxvalley.epassbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController @RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserRepository userRepository;
  private final RoleRepository roleRepo;
  private final PasswordEncoder passwordEncoder;


  @GetMapping
  public List<User> getUsers (){
    return this.userRepository.findAll();
  }

  @GetMapping("/{userId}")
  public User getUserById (@PathVariable Long userId){
    return this.userRepository.getById(userId);
  }


  @GetMapping("/getByPhone/{username}")
  public User getByUsername (@PathVariable String username){
    return this.userRepository.findByUsername(username);
  }


  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@RequestBody User user,
                                                     @PathVariable Long userId){
    User mUser = userRepository.getById(userId);
    mUser = user;
    userRepository.save(mUser);

    return new ResponseEntity<>("User updated", HttpStatus.OK);
    
  }


  @DeleteMapping("{userId}")
  void deleteUser (@PathVariable Long userId){
    this.userRepository.deleteById(userId);
  }

}

