package com.disl.boilerplate.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.disl.boilerplate.constants.AppConstants;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.models.User;
import com.disl.boilerplate.services.PrivilegeService;
import com.disl.boilerplate.services.RoleService;
import com.disl.boilerplate.services.UserService;


@Component
public class InitialDataLoader implements ApplicationListener<ApplicationContextEvent>{
	private final PasswordEncoder passwordEncoder;

	private boolean alreadySetup = false;

	@Autowired
	PrivilegeService privilegeService;

	@Autowired
	RoleService roleService;

	@Autowired
	UserService loginService;

	public InitialDataLoader(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {		
		List<Privilege> superAdminPrivileges = new ArrayList<Privilege>();

		for (Map.Entry<String, String> entry : AppConstants.PERMISSSIONS.entrySet()) {
			boolean ifPrivilegeExists = this.checkIfPrivilegeExist(entry.getKey());
			if (ifPrivilegeExists == false) {
				Privilege newPrivilege = privilegeService.createPrivilege(entry.getKey(),entry.getValue());
				superAdminPrivileges.add(newPrivilege);
			}
		}

		if (alreadySetup == true || checkIfRoleExist(AppConstants.INITIALROLE) == true ) {
			Role superAdminRole = roleService.findRoleByName(AppConstants.INITIALROLE);
			superAdminRole.getPrivileges().addAll(superAdminPrivileges);
			roleService.saveRole(superAdminRole);
			return;
		}

		List<Privilege> consumerPrivileges = new ArrayList<Privilege>();
		Privilege newPrivilege = privilegeService.createPrivilege(AppConstants.consumerPermission,AppConstants.consumerPermissionDesc);
		superAdminPrivileges.add(newPrivilege);
		consumerPrivileges.add(newPrivilege);

		roleService.createRole(AppConstants.consumerRole,RoleType.USER,null, consumerPrivileges);
		roleService.createRole(AppConstants.INITIALROLE,RoleType.ADMIN,null, superAdminPrivileges);
		Set<Role> roles = new HashSet<>();
		Role role = roleService.findRoleByName(AppConstants.INITIALROLE);
		if (role != null) {
			roles.add(role);
		}
		User superAdminUser = new User();
		superAdminUser.setEmail(AppConstants.USERNAME);
		superAdminUser.setPassword(passwordEncoder.encode(AppConstants.PASSWORD));
		superAdminUser.setRoles(roles);
		superAdminUser.setName(AppConstants.INITIALROLE);
		loginService.saveUser(superAdminUser);
		alreadySetup = true;
	}

	private boolean checkIfPrivilegeExist (String privilegeName) {
		Privilege privilege = privilegeService.findByPrivilegeName(privilegeName);
		if (privilege == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean checkIfRoleExist (String roleName) {
		Role role = roleService.findRoleByName(roleName);
		if (role == null) {
			return false;
		} else {
			return true;
		}
	}

}