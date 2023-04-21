package com.dxvalley.epassbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dxvalley.epassbook.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
