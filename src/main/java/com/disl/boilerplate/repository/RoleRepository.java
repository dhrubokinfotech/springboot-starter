package com.disl.boilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.entities.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    
    Optional<Role> findByRoleType(RoleType roleType);
    
    List<Role> findAllByRoleType(RoleType roleType);
}