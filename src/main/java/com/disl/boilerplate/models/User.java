package com.disl.boilerplate.models;

import java.util.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import com.disl.boilerplate.constants.AppTables;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = AppTables.user)
public class User extends AuditModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = AppTables.userTable.id)
	private long id;

	@Column(name = AppTables.userTable.email, nullable = false, unique = true)
	private String email;

	@Column(length = 255)
	@JsonIgnore
	@Size(max = 120)
	private String password;

	@Column(name = AppTables.userTable.passwordResetToken, nullable = true)
	private String passwordResetToken;

	@Column(length = 255)
	private String name;

	
	@JoinTable(
		name = AppTables.userRole,
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = AppTables.roleTable.roleId)
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
}