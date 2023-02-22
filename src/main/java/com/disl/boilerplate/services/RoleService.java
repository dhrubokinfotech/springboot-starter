package com.disl.boilerplate.services;

import com.disl.boilerplate.entities.Privilege;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	public Role findRoleByName(String roleName){
		Optional<Role> role = roleRepository.findByRoleName(roleName);
		if (role.isPresent()) {
			return role.get();
		} else {
			return null;
		}
	}
	
	public Role findByRoleType(RoleType roleType) {
		Optional<Role> role = roleRepository.findByRoleType(roleType);
		return role.isPresent() ? role.get() : null;
	}
	
	public List<Role> findAllByRoleType(RoleType roleType) {
		return roleRepository.findAllByRoleType(roleType);
	}
	
	public Role findRoleById (long id) {
		Optional<Role> role = roleRepository.findById(id);
		if (role.isPresent()) {
			return role.get();
		} else {
			return null;
		}
	}
	
	public Role createRole(String roleName , RoleType roleType, String description, Collection<Privilege> privileges) {
		Role role = new Role();
		role.setRoleName(roleName);
		role.setPrivileges(privileges);
		role.setRoleType(roleType);
		role.setDescription(description);
		return roleRepository.save(role);
	}
	
	public List<Role> findAllRoles (String roleType) {
		if(!roleType.isEmpty()) {
			return roleRepository.findAllByRoleType(RoleType.valueOf(String.valueOf(roleType)));
		}
		
		return roleRepository.findAll();
	}
	
	public Role saveRole (Role role) {
		return roleRepository.save(role);
	}
	
	public Role deleteRole(long id) {
		Optional<Role> role = roleRepository.findById(id);
		if (role.isPresent()) {
			roleRepository.delete(role.get());
			return role.get();
		} else {
			return null;
		}
	}
}
