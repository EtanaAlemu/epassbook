package com.dxvalley.epassbook.nedaj;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NedajRepository extends JpaRepository<Nedaj, Long> {
    Optional<Nedaj> findByMessageId(String messageId);
}