package com.disl.boilerplate.models;

import java.util.Collection;
import javax.persistence.*;

import com.disl.boilerplate.constants.AppTables;
import com.disl.boilerplate.enums.RoleType;


@Entity
@Table(name = AppTables.role)
public class Role extends AuditModel<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = AppTables.roleTable.roleId)
	private Long id;

	@Column(name = AppTables.roleTable.roleName)
	private String roleName;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinTable(name = AppTables.rolePrivilege, joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
	private Collection<Privilege> privileges;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Column(columnDefinition="TEXT")
	private String description;

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
