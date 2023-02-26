package com.disl.starter.entities;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.PrivilegeTable;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.PRIVILEGE_NAME)
public class Privilege extends AuditModel<String> {

	@Column(name = PrivilegeTable.NAME)
	private String name;
	
	@Column(name = PrivilegeTable.DESC_NAME)
	private String descName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}
}
