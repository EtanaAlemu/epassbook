package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dxvalley.epassbook.models.Account;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a from Account as a WHERE a.isMainAccount = TRUE")
    Account findPrimaryAccount(Long userId);
    Optional< Account> findByAccountNumber(String accountNumber);
}