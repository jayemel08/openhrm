package com.dhruvchaudhary.hrm.dto;

import com.dhruvchaudhary.hrm.model.CompensatoryOff;

public class CompensatoryOffDTO {
	private String id;
	
	private String empCode;
	
	private String applicationId;
	
	private String appliedAgainstDate;  //date on which emp has worked extra hrs

	private String leaveId;
	
	private String active;
	
	public CompensatoryOffDTO() {
		this.id = this.empCode = this.applicationId = this.appliedAgainstDate = this.leaveId = this.active = null ;
	}
	
	public CompensatoryOffDTO(CompensatoryOff compensatoryOff) {
		this.id = Integer.toString(compensatoryOff.getId());
		this.empCode = Integer.toString(compensatoryOff.getEmployee().getEmpCode());
		this.applicationId = Integer.toString(compensatoryOff.getApplication().getId());
		this.appliedAgainstDate = compensatoryOff.getAppliedAgainst().toString();
		this.leaveId = Integer.toString(compensatoryOff.getLeave().getId());
		this.active = compensatoryOff.isActive()?"&nbsp;":"Pending Approval";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppliedAgainstDate() {
		return appliedAgainstDate;
	}

	public void setAppliedAgainstDate(String appliedAgainstDate) {
		this.appliedAgainstDate = appliedAgainstDate;
	}

	public String getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
}
