package com.disl.starter.controllers;

import com.disl.starter.config.CommonApiResponses;
import com.disl.starter.entities.Privilege;
import com.disl.starter.entities.Role;
import com.disl.starter.entities.User;
import com.disl.starter.enums.AscOrDescType;
import com.disl.starter.enums.RoleType;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.models.PaginationArgs;
import com.disl.starter.models.Response;
import com.disl.starter.models.requests.RoleCreateRequest;
import com.disl.starter.models.requests.RoleUpdateRequest;
import com.disl.starter.services.PrivilegeService;
import com.disl.starter.services.RoleService;
import com.disl.starter.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.disl.starter.constants.AppConstants.*;

@RestController
@CrossOrigin()
@CommonApiResponses
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PrivilegeService privilegeService;

    @Operation(summary = "Get all user roles", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))), responseCode = "200")
	@PreAuthorize("#userEmail == authentication.name")
	@GetMapping("/users")
	public ResponseEntity<Response> getUserRoles(@RequestParam("userEmail") String userEmail) {
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return Response.getResponseEntity(false,  "No user found with this user email", null);
		}

		List<Role> userRoles = new ArrayList<>(user.getRoles());
		return Response.getResponseEntity(true, "Data loaded successfully.", userRoles);
	}

    @Operation(summary = "Get all role types", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))), responseCode = "200")
	@GetMapping("/types")
	public ResponseEntity<Response> getRoleTypes() {
		List<String> types = Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList());
		return Response.getResponseEntity(true, "All active role types in the system.", types);
	}


    @Operation(summary = "Create Role", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Role.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_CREATE')")
	@PostMapping("/create")
	public ResponseEntity<Response> createRole(@RequestBody RoleCreateRequest roleRequest) {
		Role existingRole = roleService.findByRoleType(roleRequest.getRoleType());
		if (existingRole == null) {
			return Response.getResponseEntity(false, "Role with role type not found", null);
		}

		List<Privilege> privileges = Arrays.stream(roleRequest.getPrevilegeId()).mapToObj(privilegeId -> {
			Privilege privilege = privilegeService.findByPrivilegeId(privilegeId);
			if (privilege == null) {
				throw new ResponseException("Invalid privilege id: " + privilegeId);
			}

			return privilege;
		}).collect(Collectors.toList());

		Role role = roleService.createRole(
				roleRequest.getRoleName(), roleRequest.getRoleType(),
				roleRequest.getDescription(), privileges
		);

		if (role == null) {
			return Response.getResponseEntity(false, "Role not created. Please try again", null);
		}

		return Response.getResponseEntity(true, "Role creation successful.", role);
	}

    @Operation(summary = "Get all roles - paginated", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_READ')")
	@GetMapping("/all")
	public ResponseEntity<Response> getPaginatedRoles(
			@RequestParam(name = PAGE_NO, defaultValue = "0") int pageNo,
			@RequestParam(name = PAGE_SIZE, defaultValue = "20") int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = "") String sortBy,
			@RequestParam(name = ASC_OR_DESC, defaultValue = "") AscOrDescType ascOrDesc,
			@RequestParam(required = false) Map<String, Object> parameters
	) {
		PaginationArgs paginationArgs = new PaginationArgs(
				pageNo, pageSize, sortBy, ascOrDesc, parameters
		);

		return Response.getResponseEntity(
				true, "Data loaded successfully.",
				roleService.findRolesPaginated(paginationArgs)
		);
	}

    @Operation(summary = "Get all roles - total", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_READ')")
	@GetMapping("/total")
	public ResponseEntity<Response> getTotalRoles(@RequestParam(name = "roleType", defaultValue = "", required = false) String roleType) {
		return Response.getResponseEntity(
				true, "Data loaded successfully.",
				roleService.findAllRoles(roleType)
		);
	}

    @Operation(summary = "Get role by id", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Role.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_READ')")
	@GetMapping("/{id}")
	public ResponseEntity<Response> getRoleById(@PathVariable("id") long roleId) {
		return Response.getResponseEntity(
				true, "Data loaded successfully.",
				roleService.findRoleByIdWithException(roleId)
		);
	}

    @Operation(summary = "Delete Role By Id", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_DELETE')")
	@DeleteMapping("/{roleId}")
	public ResponseEntity<Response> deleteRole(@PathVariable("roleId") long id) {
		if (roleService.deleteRole(id) == null) {
			return Response.getResponseEntity(true, "Role deleted.");
		} else {
			return Response.getResponseEntity(false, "Role deletion failed. Role not found.");
		}
	}

    @Operation(summary = "Update Role", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('ROLE_UPDATE')")
	@PutMapping("/update")
	public ResponseEntity<Response> updateRole(@RequestBody @Valid RoleUpdateRequest roleUpdateRequest) {
		return Response.getResponseEntity(true, "Role updated.", roleService.updateRole(roleUpdateRequest));
	}
}
