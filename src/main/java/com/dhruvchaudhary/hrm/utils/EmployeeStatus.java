package com.dhruvchaudhary.hrm.utils;

public enum EmployeeStatus {
	ON_ROLES(0, "On Roles"), 
	ON_PROBATION(1, "On Probation"), 
	IN_NOTICE_PERIOD(2, "On Notice"), 
	RESIGNED(3, "Resigned");
	
	private int id;
	private String description;
	
	private EmployeeStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
