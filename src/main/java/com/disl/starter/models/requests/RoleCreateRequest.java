package com.disl.starter.models.requests;

import com.disl.starter.enums.RoleType;

public class RoleCreateRequest {

	private String roleName;
	private long[] previlegeId;
	private RoleType roleType;
	private String description;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public long[] getPrevilegeId() {
		return previlegeId;
	}

	public void setPrevilegeId(long[] previlegeId) {
		this.previlegeId = previlegeId;
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
}
