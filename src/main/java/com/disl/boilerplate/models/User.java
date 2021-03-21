package com.disl.boilerplate.models;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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

	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = AppTables.userRole, 
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = AppTables.roleTable.roleId))
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