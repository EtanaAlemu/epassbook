package com.dxvalley.epassbook.serviceImpl;

import com.dxvalley.epassbook.exceptions.ResourceAlreadyExistsException;
import com.dxvalley.epassbook.models.Account;
import com.dxvalley.epassbook.models.Address;
import com.dxvalley.epassbook.models.Role;
import com.dxvalley.epassbook.models.Users;
import com.dxvalley.epassbook.repositories.AccountRepository;
import com.dxvalley.epassbook.repositories.AddressRepository;
import com.dxvalley.epassbook.repositories.RoleRepository;
import com.dxvalley.epassbook.repositories.UserRepository;
import com.dxvalley.epassbook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users registerUser(Users tempUser) {

        var result = userRepository.findByUsername(tempUser.getUsername());
        if (result != null) {
            throw new ResourceAlreadyExistsException("This username is already used.");
        }

        Users users = new Users();

        List<Role> roles = new ArrayList<Role>(1);
        roles.add(roleRepository.findByRoleName("user"));

        users.setUsername(tempUser.getUsername());
        users.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        users.setRoles(roles);
        users.setFullName(tempUser.getFullName());
        users.setEmail(tempUser.getEmail());
        users.setEmailConfirmed(false);
        users.setDateOfBirth(tempUser.getDateOfBirth());
        users.setGender(tempUser.getGender());
        users.setImageUrl(tempUser.getImageUrl());
        users.setCreatedAt(LocalDateTime.now().toString());
        users.setAccessFailedCount(0);
        users.setTwoFactorEnabled(false);
        users.setIsEnabled(true);
        users.setPhoneNumber(tempUser.getPhoneNumber());
        users.setLanguageCode(0);

        Address address = addressRepository.save(tempUser.getAddress());
        users.setAddress(address);

        ArrayList<Account> accounts = new ArrayList<>();
        for (var account : tempUser.getAccounts()){
            accounts.add(account);
        }
        List<Account>  accountList = accountRepository.saveAll(accounts);
        users.setAccounts(accountList);
        return userRepository.save(users);
    }

}
