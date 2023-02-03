package com.dxvalley.epassbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dxvalley.epassbook.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getById(Long id);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}