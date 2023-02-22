package com.disl.boilerplate.services;

import com.disl.boilerplate.entities.Privilege;
import com.disl.boilerplate.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {

	@Autowired
    PrivilegeRepository privilegeDao;
	
	public Privilege findByPrivilegeName (String name) {
		return privilegeDao.findByName(name).orElse(null);
	}
	
	public Privilege createPrivilege(String name,String descName) {
		Privilege privilege = new Privilege();
		privilege.setName(name);
		privilege.setDescName(descName);
		return privilegeDao.save(privilege);
	}
	
	public List<Privilege> viewAllPrivileges() {
		return privilegeDao.findAll();
	}
	
	public Privilege findByPrivilegeId (long id) {
		Optional <Privilege> privilege = privilegeDao.findById(id);
		if (privilege.isPresent()) {
			return privilege.get();
		} else {
			return null;
		}
	}
}
