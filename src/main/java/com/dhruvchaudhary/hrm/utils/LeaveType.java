package com.dhruvchaudhary.hrm.utils;

public enum LeaveType {
	LEAVE_PAID(0, "Paid Leave"), 
	LEAVE_AGAINST_COMP_OFF(1, "Leave against Compensatory Off"), 
	LEAVE_WITHOUT_PAY(2, "Leave without pay");
	
	private int id;
	private String description;
	
	private LeaveType(int id, String description) {
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
