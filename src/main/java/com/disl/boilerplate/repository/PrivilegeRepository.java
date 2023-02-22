package com.disl.boilerplate.repository;

import com.disl.boilerplate.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
	Optional<Privilege> findByName(String name);

	List<Privilege> findAllByNameIn(List<String> names);
}
