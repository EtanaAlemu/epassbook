package com.dxvalley.epassbook.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional <Users> findByUserId(Long userId);
    Optional<Users> findByPhoneNumber(String phoneNumber);
}