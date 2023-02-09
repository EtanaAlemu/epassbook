package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dxvalley.epassbook.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}