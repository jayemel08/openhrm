package com.dhruvchaudhary.hrm.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import com.dhruvchaudhary.hrm.customValidators.annotations.MustBeInteger;
import com.dhruvchaudhary.hrm.customValidators.annotations.MustBeManager;

public class EmployeeDTO {

	@MustBeInteger(min = 1)
	private String empCode;
	
	@NotEmpty
	private String name;
	
	private String password;
	
	private String newPassword;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String statusCode;  //from radio
	
	private String status; //never in form
	
	@MustBeManager
	private String mgrCode;
	private String mgrName; //never in form
	
	@NotEmpty
	private String doj;  //using datepicker
	
	@MustBeInteger(min=0, max=366)
	private String leavesTotal;
	
	private String leavesRemaining;
/*	private boolean isManager = false;
	private boolean isHR = false;*/
	/*private Map<String, List<EmployeeAdditionalRoles>> roles;*/
	private String[] roles;  //using checkboxes
	/*private List<EmployeeAdditionalRoles> roles;*/
	
/*	public List<EmployeeAdditionalRoles> getRoles() {
		return roles;
	}

	public void setRoles(List<EmployeeAdditionalRoles> roles) {
		this.roles = roles;
	}*/

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String[] getRoles() {
		return roles;
	}

	public String getEmpCode() {
		return empCode;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public String getMgrCode() {
		if(mgrCode == null)
			return "";
		else
			return mgrCode;
	}
	
	public String getDoj() {
		return doj;
	}
	
	public String getLeavesTotal() {
		return leavesTotal;
	}
	
	public String getLeavesRemaining() {
		return leavesRemaining;
	}
	
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setStatusCode(String status) {
		this.statusCode = status;
	}
	
	public void setMgrCode(String manager) {
		this.mgrCode = manager;
	}
	
	public void setDoj(String doj) {
		this.doj = doj;
	}
	
	public void setLeavesTotal(String leavesTotal) {
		this.leavesTotal = leavesTotal;
	}
	
	public void setLeavesRemaining(String leavesRemaining) {
		this.leavesRemaining = leavesRemaining;
	}
	
	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/*public Map<String, List<EmployeeAdditionalRoles>> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, List<EmployeeAdditionalRoles>> roles) {
		this.roles = roles;
	}*/	
}
