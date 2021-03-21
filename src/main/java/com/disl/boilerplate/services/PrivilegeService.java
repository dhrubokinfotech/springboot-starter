package com.disl.boilerplate.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.repository.PrivilegeDao;

@Service
public class PrivilegeService {

	@Autowired
	PrivilegeDao privilegeDao;
	
	public Privilege findByPrivilegeName (String name) {
		Optional<Privilege> privilege = privilegeDao.findByName(name);
		if (privilege.isPresent()) {
			return privilege.get();
		} else {
			return null;
		}
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
