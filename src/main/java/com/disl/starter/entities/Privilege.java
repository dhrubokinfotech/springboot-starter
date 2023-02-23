package com.disl.starter.entities;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.PrivilegeTable;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.privilege)
public class Privilege extends AuditModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = PrivilegeTable.id)
	private long id;
	
	@Column(name = PrivilegeTable.name)
	private String name;
	
	@Column(name = PrivilegeTable.descName)
	private String descName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
