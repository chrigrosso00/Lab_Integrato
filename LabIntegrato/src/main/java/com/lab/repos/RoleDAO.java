package com.lab.repos;

import com.lab.entities.Role;
import com.lab.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
