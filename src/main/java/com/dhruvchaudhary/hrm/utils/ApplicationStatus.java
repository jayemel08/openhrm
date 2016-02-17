package com.dhruvchaudhary.hrm.utils;

public enum ApplicationStatus {
	PENDING(0, "Pending"), APPROVED(1, "Approved"), REJECTED(2, "Rejected");
	
	private int id;
	private String description;
	
	private ApplicationStatus(int id, String description) {
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
