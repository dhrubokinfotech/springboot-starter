package com.disl.boilerplate.config;

import com.disl.boilerplate.constants.AppConstants;
import com.disl.boilerplate.entities.Privilege;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.entities.User;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.services.PrivilegeService;
import com.disl.boilerplate.services.RoleService;
import com.disl.boilerplate.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InitialDataLoader implements ApplicationListener<ApplicationContextEvent>{

	private boolean alreadySetup = false;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService loginService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PrivilegeService privilegeService;

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		List<Privilege> superAdminPrivileges = new ArrayList<>();

		for (Map.Entry<String, String> entry : AppConstants.PERMISSIONS.entrySet()) {
			boolean ifPrivilegeExists = this.checkIfPrivilegeExist(entry.getKey());

			if (!ifPrivilegeExists) {
				Privilege newPrivilege = privilegeService.createPrivilege(entry.getKey(),entry.getValue());
				superAdminPrivileges.add(newPrivilege);
			}
		}

		if(!checkIfPrivilegeExist(AppConstants.INITIAL_ROLE)) {
			Role superAdminRole = roleService.findRoleByName(AppConstants.INITIAL_ROLE);
			superAdminRole.getPrivileges().addAll(superAdminPrivileges);
			roleService.saveRole(superAdminRole);
		}

		if (alreadySetup || checkIfSuperAdminExist()) {
			return;
		}

		List<Privilege> consumerPrivileges = new ArrayList<>();
		Privilege newPrivilege = privilegeService.createPrivilege(AppConstants.consumerPermission,AppConstants.consumerPermissionDesc);
		superAdminPrivileges.add(newPrivilege);
		consumerPrivileges.add(newPrivilege);

		roleService.createRole(AppConstants.userRole,RoleType.USER,null, consumerPrivileges);
		roleService.createRole(AppConstants.INITIAL_ROLE,RoleType.ADMIN,null, superAdminPrivileges);

		Set<Role> roles = new HashSet<>();
		Role role = roleService.findRoleByName(AppConstants.INITIAL_ROLE);
		if (role != null) {
			roles.add(role);
		}

		User superAdminUser = new User();
		superAdminUser.setRoles(roles);
		superAdminUser.setVerified(true);
		superAdminUser.setBanned(false);
		superAdminUser.setName(AppConstants.INITIAL_ROLE);
		superAdminUser.setEmail(AppConstants.INITIAL_USERNAME);
		superAdminUser.setPassword(passwordEncoder.encode(AppConstants.INITIAL_PASSWORD));
		loginService.saveUser(superAdminUser);

		alreadySetup = true;
	}

	private boolean checkIfPrivilegeExist (String privilegeName) {
		return privilegeService.findByPrivilegeName(privilegeName) != null;
	}

	private boolean checkIfSuperAdminExist () {
		return loginService.findByEmail(AppConstants.INITIAL_USERNAME) != null;
	}

	private boolean checkIfRoleExist (String roleName) {
		return roleService.findRoleByName(roleName) != null;
	}
}
