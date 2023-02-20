package com.disl.boilerplate.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.disl.boilerplate.constants.AppTables;

@Entity
@Table(name = AppTables.privilege)
public class Privilege extends AuditModel<String>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = AppTables.privilegeTable.id)
	private long id;
	
	@Column(name = AppTables.privilegeTable.name)
	private String name;
	
	@Column(name = AppTables.privilegeTable.descName)
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
