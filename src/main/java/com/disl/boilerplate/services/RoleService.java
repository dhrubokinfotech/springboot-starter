package com.disl.boilerplate.services;

import com.disl.boilerplate.constants.AppUtils;
import com.disl.boilerplate.entities.Privilege;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.exceptions.NotFoundException;
import com.disl.boilerplate.exceptions.ResponseException;
import com.disl.boilerplate.models.PaginationArgs;
import com.disl.boilerplate.models.requests.RoleUpdateRequest;
import com.disl.boilerplate.repository.RoleRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeService privilegeService;

	public Role findByRoleType(RoleType roleType) {
		return roleRepository.findTopByRoleType(roleType).orElse(null);
	}
	
	public Role findRoleByName(String roleName){
		return roleRepository.findTopByRoleName(roleName).orElse(null);
	}

	Role findRoleByNameWithException(String email) {
		return roleRepository.findTopByRoleName(email).orElseThrow(() ->
				new ResponseException(HttpStatus.FORBIDDEN, "No role found with this name")
		);
	}
	
	public List<Role> findAllByRoleType(RoleType roleType) {
		return roleRepository.findAllByRoleType(roleType);
	}

	public Role updateRole(RoleUpdateRequest roleUpdateRequest) {
		Role role = roleRepository.findById(roleUpdateRequest.getId()).orElse(null);
		if (role == null) {
			throw new NotFoundException(Role.class);
		}

		List<Privilege> privileges = new ArrayList<>();

		for (Long privilegeId : roleUpdateRequest.getPrevilegeId()) {
			Privilege privilege = privilegeService.findByPrivilegeId(privilegeId);
			if (privilege != null) {
				privileges.add(privilege);
			} else {
				throw new ResponseException("Privilege id: " + privilegeId + " not found");
			}
		}

		if (role.getRoleType() != roleUpdateRequest.getRoleType()) {
			Role existingRole = roleRepository.findTopByRoleType(roleUpdateRequest.getRoleType()).orElse(null);
			if (existingRole != null) {
				throw new ResponseException("Role with role type already exists.");
			}
		}

		role.setRoleName(roleUpdateRequest.getRoleName());
		role.setPrivileges(privileges);
		role.setDescription(roleUpdateRequest.getDescription());
		return roleRepository.save(role);
	}
	
	public Role findRoleById (long id) {
		return roleRepository.findById(id).orElse(null);
	}

	public Role findRoleByIdWithException (long id) {
		return roleRepository.findById(id).orElseThrow(() -> new NotFoundException(Role.class));
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
			return roleRepository.findAllByRoleType(RoleType.valueOf(roleType));
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

	public Page<Role> findRolesPaginated(PaginationArgs paginationArgs) {
		Pageable pageable = AppUtils.getPageable(paginationArgs);
		Map<String, Object> specParameters = AppUtils.getParameters(paginationArgs.getParameters());

		if(!specParameters.isEmpty()) {
			Specification<Role> specification = Specification.where((root, query, criteriaBuilder) -> {
				List<Predicate> predicates = new ArrayList<>();

				for (Map.Entry<String, Object> entry : specParameters.entrySet()) {
					String filterBy = entry.getKey();
					String filterWith = entry.getValue().toString();

					if (filterWith != null && !filterWith.isEmpty()) {
						Class<?> type = root.get(filterBy).getJavaType();

						if (type.equals(Long.class)) {
							return criteriaBuilder.equal(root.get(filterBy), Long.valueOf(filterWith));
						} else if (type.equals(RoleType.class)) {
							predicates.add(criteriaBuilder.equal(root.get(filterBy), RoleType.valueOf(filterWith)));
						} else if (type.equals(Collection.class)) {
							Join<Role, Collection<Privilege>> resource = root.join("privileges");
							predicates.add(criteriaBuilder.equal(resource.get("name"), filterWith));
						} else if (type.equals(String.class)) {
							predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(filterBy)), "%" + filterWith.toUpperCase() + "%"));
						} else if (type.equals(LocalDateTime.class)) {
							LocalDate localDate = LocalDate.parse(filterWith);
							LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
							LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
							predicates.add(criteriaBuilder.between(root.get(filterBy), startDateTime, endDateTime));
						} else {
							predicates.add(criteriaBuilder.equal(root.get(filterBy), filterWith));
						}
					}
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
			});

			return roleRepository.findAll(specification, pageable);
		}

		return roleRepository.findAll(pageable);
	}
}
