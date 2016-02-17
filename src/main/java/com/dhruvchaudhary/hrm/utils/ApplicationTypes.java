package com.dhruvchaudhary.hrm.utils;

public enum ApplicationTypes {
	LEAVE(0, "Leave"), 
	LEAVE_AGAINST_COMPENSATORY_OFF(1, "Leave against compensatory off"), 
	COMPENSATORY_OFF(2, "Compensatory off"), 
	WORK_FROM_HOME(3, "Work from home");
	
	private int id;
	private String description;
	
	private ApplicationTypes(int id, String description) {
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
