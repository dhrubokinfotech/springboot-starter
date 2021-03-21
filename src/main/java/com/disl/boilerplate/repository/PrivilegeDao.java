package com.disl.boilerplate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.disl.boilerplate.models.Privilege;


@Repository
public interface PrivilegeDao extends JpaRepository<Privilege,Long> {
	Optional<Privilege> findByName(String name);
	List<Privilege> findAllByNameIn(List<String> names);
}
