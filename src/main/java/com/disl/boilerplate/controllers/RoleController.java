package com.disl.boilerplate.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.disl.boilerplate.config.AppProperties;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.models.User;
import com.disl.boilerplate.models.requests.RoleCreateRequest;
import com.disl.boilerplate.models.requests.RoleUpdateRequest;
import com.disl.boilerplate.payloads.Response;
import com.disl.boilerplate.services.PrivilegeService;
import com.disl.boilerplate.services.RoleService;
import com.disl.boilerplate.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin()
@RequestMapping("role")

public class RoleController {

	@Autowired
	private UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	PrivilegeService privilegeService;

	@Autowired
	AppProperties appProperties;

	@ApiOperation(value = "Get all user roles", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Role.class, responseContainer = "List"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("#userEmail == authentication.name")
	@GetMapping("/users")
	public Response getUserRoles(@RequestParam("userEmail") String userEmail) {
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return new Response(HttpStatus.NOT_FOUND, false, "No user found with this userEmail", null);
		}
		List<Role> userRoles = new ArrayList<Role>(user.getRoles());
		return new Response(HttpStatus.OK, true, "Data loaded successfully.", userRoles);
	}

	@ApiOperation(value = "Get all role types", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Role.class, responseContainer = "List"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@GetMapping("/types")
	public Response getRoleTypes() {
		RoleType[] alltypes = RoleType.values();
		List<String> types = Arrays.asList(alltypes).stream().map(item -> item.name()).collect(Collectors.toList());
		return new Response(HttpStatus.OK, true, "All active role types in the system.", types);
	}

	@ApiOperation(value = "Create role", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Role.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("hasPermission(null, 'ROLE', 'CREATE')")
	@PostMapping
	public Response createRole(@RequestBody RoleCreateRequest roleRequest) {
		List<Privilege> privileges = new ArrayList<Privilege>();
		for (Long privilegeId : roleRequest.getPrevilegeId()) {
			Privilege privilege = privilegeService.findByPrivilegeId(privilegeId);

			if (privilege != null) {
				privileges.add(privilege);
			} else {
				return new Response(HttpStatus.BAD_REQUEST, false, "Invalid privilege id: " + privilegeId, null);
			}
		}

		Role existingRole = roleService.findByRoleType(roleRequest.getRoleType());
		if (existingRole != null) {
			return new Response(HttpStatus.BAD_REQUEST, false, "Role with role type already exists.", null);
		}

		Role role = roleService.createRole(roleRequest.getRoleName(), roleRequest.getRoleType(),
				roleRequest.getDescription(), privileges);

		if (role == null) {
			return new Response(HttpStatus.BAD_REQUEST, false, "Role not created. Please try again", null);
		}

		return new Response(HttpStatus.OK, true, "Role creation successful.", role);
	}

	@ApiOperation(value = "Get all roles [admin]", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Role.class, responseContainer = "List"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
	@GetMapping
	public Response getRoles(@RequestParam(name = "roleType", defaultValue = "", required = false) String roleType) {
		return new Response(HttpStatus.OK, true, "Data loaded successfully.", roleService.findAllRoles(roleType));
	}

	@ApiOperation(value = "Get role by id. [admin]", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Role.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
	@GetMapping("/{id}")
	public Response getRoleById(@PathVariable("id") long roleId) {
		Role role = roleService.findRoleById(roleId);
		if (role == null) {
			return new Response(HttpStatus.BAD_GATEWAY, false, "No role found with this id", null);
		}
		return new Response(HttpStatus.OK, true, "Data loaded successfully.", role);
	}

	@ApiOperation(value = "Delete Role By Id.", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Response.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("hasPermission(null, 'ROLE', 'DELETE')")
	@DeleteMapping("/{roleId}")
	public Response deleteRole(@PathVariable("roleId") long id) {
		if (roleService.deleteRole(id) == null) {
			return new Response(HttpStatus.OK, true, "Role deleted.", null);
		} else {
			return new Response(HttpStatus.OK, false, "Role deletion failed. Role not found.", null);
		}
	}

	@ApiOperation(value = "Update Role [admin]", authorizations = { @Authorization(value = "jwtToken") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Response.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	@PreAuthorize("hasPermission(null, 'ROLE', 'UPDATE')")
	@PutMapping
	public Response updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
		Role role = roleService.findRoleById(roleUpdateRequest.getId());
		List<Privilege> privileges = new ArrayList<Privilege>();

		for (Long privilegeId : roleUpdateRequest.getPrevilegeId()) {
			Privilege privilege = privilegeService.findByPrivilegeId(privilegeId);
			if (privilege != null) {
				privileges.add(privilege);
			} else {
				return new Response(HttpStatus.BAD_REQUEST, false, "Privilege id: " + privilegeId + " not found", null);
			}
		}

		if (role != null) {
			if (role.getRoleType() != roleUpdateRequest.getRoleType()) {
				Role existingRole = roleService.findByRoleType(roleUpdateRequest.getRoleType());
				if (existingRole != null) {
					return new Response(HttpStatus.BAD_REQUEST, false, "Role with role type already exists.", null);
				}
			}

			role.setRoleName(roleUpdateRequest.getRoleName());
			role.setPrivileges(privileges);
			role.setDescription(roleUpdateRequest.getDescription());
			roleService.saveRole(role);
			return new Response(HttpStatus.OK, true, "Role updated.", null);
		} else {
			return new Response(HttpStatus.BAD_REQUEST, false, "Role not found", null);
		}
	}
	
}
