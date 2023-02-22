package com.disl.boilerplate.security;

import com.disl.boilerplate.entities.Privilege;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
	private Long id;
	private String name;
	private String email;
	private String password;
	private boolean verified;
	private Set<Role> roles;
	private Collection<? extends GrantedAuthority> authorities;

	private CustomUserDetails(
			Long id, String email, String name, String password, Boolean verified,
			Set<Role> roles, Collection<? extends GrantedAuthority> authorities
	) {
		super();
		this.id = id;
		this.name = name;
		this.roles = roles;
		this.email = email;
		this.password = password;
		this.verified = verified;
		this.authorities = authorities;
	}

	public static CustomUserDetails create(User user) {
		List<Role> roles = new ArrayList<>(user.getRoles());
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<Privilege> privileges = new ArrayList<>();

		for(Role role : roles) {
			privileges.addAll(role.getPrivileges());
		}

		for (Privilege privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege.getName()));
		}

		return new CustomUserDetails(
				user.getId(), user.getEmail(), user.getName(), user.getPassword(),
				user.getVerified(), user.getRoles(), authorities
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
