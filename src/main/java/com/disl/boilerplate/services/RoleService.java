package com.disl.boilerplate.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.repository.RoleDao;


@Service
public class RoleService {
	
	@Autowired
	RoleDao roleDao;
	
	public Role findRoleByName(String roleName){
		Optional<Role> role = roleDao.findByRoleName(roleName);
		if (role.isPresent()) {
			return role.get();
		} else {
			return null;
		}
	}
	
	public Role findByRoleType(RoleType roleType) {
		Optional<Role> role = roleDao.findByRoleType(roleType);
		return role.isPresent() ? role.get() : null;
	}
	
	public List<Role> findAllByRoleType(RoleType roleType) {
		return roleDao.findAllByRoleType(roleType);
	}
	
	public Role findRoleById (long id) {
		Optional<Role> role = roleDao.findById(id);
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
		return roleDao.save(role);
	}
	
	public List<Role> findAllRoles (String roleType) {
		if(!roleType.isEmpty()) {
			return roleDao.findAllByRoleType(RoleType.valueOf(String.valueOf(roleType)));
		}
		
		return roleDao.findAll();
	}
	
	public Role saveRole (Role role) {
		return roleDao.save(role);
	}
	
	public Role deleteRole(long id) {
		Optional<Role> role = roleDao.findById(id);
		if (role.isPresent()) {
			roleDao.delete(role.get());
			return role.get();
		} else {
			return null;
		}
	}
}
