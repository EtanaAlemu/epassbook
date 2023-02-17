package com.dxvalley.epassbook.repositories;

import com.dxvalley.epassbook.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findByUserId(Long userId);
    Users findByPhoneNumber(String phoneNumber);
}