package com.disl.boilerplate.entities;

import com.disl.boilerplate.constants.AppTables;
import com.disl.boilerplate.constants.AppTables.UserTable;
import com.disl.boilerplate.models.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = AppTables.user)
public class User extends AuditModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = UserTable.id)
	private long id;

	@Column(name = UserTable.email, nullable = false, unique = true)
	private String email;

	@Column(name = UserTable.password)
	@JsonIgnore
	@Size(max = 120)
	private String password;

	@Column(name = UserTable.passwordResetToken)
	private String passwordResetToken;

	@Column(name = UserTable.name)
	private String name;

	@Column (name = UserTable.verified)
	private Boolean verified = false;
	
	@JoinTable(
		name = AppTables.userRole,
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = AppTables.RoleTable.roleId)
	)
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
}