package com.disl.boilerplate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disl.boilerplate.models.Privilege;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDao extends JpaRepository<Privilege,Long> {
	Optional<Privilege> findByName(String name);
	List<Privilege> findAllByNameIn(List<String> names);
}
