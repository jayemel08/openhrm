package com.dhruvchaudhary.hrm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "additional_roles")
public class EmployeeAdditionalRoles {

	@Id
	@GeneratedValue
	private int id;
	@Column(nullable=false, unique=true)
	private String description;
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
