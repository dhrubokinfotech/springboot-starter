package com.disl.boilerplate.entities;

import com.disl.boilerplate.constants.AppTables;
import com.disl.boilerplate.constants.AppTables.RoleTable;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.models.AuditModel;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = AppTables.role)
public class Role extends AuditModel<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = RoleTable.roleId)
	private Long id;

	@Column(name = RoleTable.roleName)
	private String roleName;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinTable(
			name = AppTables.rolePrivilege,
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "privilege_id")
	)
	private Collection<Privilege> privileges;

	@Column(name = RoleTable.roleType)
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Column(name = RoleTable.description, columnDefinition="TEXT")
	private String description;

	@Column(name = RoleTable.imageUrl)
	private String imageUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Collection<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
