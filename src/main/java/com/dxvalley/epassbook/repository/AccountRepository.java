package com.dxvalley.epassbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dxvalley.epassbook.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}