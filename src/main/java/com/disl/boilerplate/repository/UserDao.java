package com.disl.boilerplate.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.models.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	Optional<User> findByEmail(String email);
	Optional<User> findByPasswordResetToken (String token);
	List<User> findByRoles(Role role);
	List<User> findByRolesIn(Role[] roles);
	
}
