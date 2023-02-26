package com.disl.starter.entities;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.UserTable;
import com.disl.starter.models.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = AppTables.USER_NAME)
public class User extends AuditModel<String> {

	@Column(name = UserTable.EMAIL, nullable = false, unique = true)
	private String email;

	@Column(name = UserTable.password)
	@JsonIgnore
	@Size(max = 120)
	private String password;

	@Column(name = UserTable.PASSWORD_RESET_TOKEN)
	private String passwordResetToken;

	@Column(name = UserTable.NAME)
	private String name;

	@Column (name = UserTable.VERIFIED)
	private Boolean verified = false;

	@Column (name = UserTable.BANNED)
	private Boolean banned = false;

	@JoinTable(
		name = AppTables.USER_ROLE_NAME,
		joinColumns = @JoinColumn(name = UserTable.USER_ID),
		inverseJoinColumns = @JoinColumn(name = UserTable.ROLE_ID)
	)
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();

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
		return verified != null && verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Boolean getBanned() {
		return banned != null && banned;
	}

	public void setBanned(Boolean banned) {
		this.banned = banned;
	}
}