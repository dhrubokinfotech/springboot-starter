package com.disl.boilerplate.security;

import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.email = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static CustomUserDetails create(User user) {
		List<Role> roles = new ArrayList<>(user.getRoles());
		List<Privilege> privileges = new ArrayList<>();

		for(Role role : roles) {
			privileges.addAll(role.getPrivileges());
		}

		List<SimpleGrantedAuthority> authorities = privileges.stream()
				.map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
				.collect(Collectors.toList());

		return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities);
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
}
