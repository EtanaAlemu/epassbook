package com.dxvalley.epassbook.repositories;

import com.dxvalley.epassbook.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional <Users> findByUserId(Long userId);
    Optional<Users> findByPhoneNumber(String phoneNumber);
}