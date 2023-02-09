package com.dxvalley.epassbook.repositories;

import com.dxvalley.epassbook.models.Passcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasscodeRepository extends JpaRepository<Passcode, Long> {
    Passcode findByUserId(Long id);
}
