package com.disl.boilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.entities.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findTopByRoleName(String roleName);
    List<Role> findAllByRoleType(RoleType roleType);
    Optional<Role> findTopByRoleType(RoleType roleType);
}