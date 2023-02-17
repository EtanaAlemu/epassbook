package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dxvalley.epassbook.models.Account;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a from Account as a WHERE user.userId = :userId")
    List<Account> findByUser(Long userId);
}