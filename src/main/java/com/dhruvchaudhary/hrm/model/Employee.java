package com.dhruvchaudhary.hrm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dhruvchaudhary.hrm.utils.EmployeeStatus;

@Entity
@Table(name = "employee")
public class Employee{

	@Id
	@Column(updatable = false)
	private int empCode;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String password;
	@Column(unique = true, nullable = false)
	private String email;
	/*@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "status")
	private EmployeeStatus status;*/
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private EmployeeStatus status;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "mgrCode")
	private Employee manager;
	@Temporal(TemporalType.DATE)
	private Date doj;
	@Column(nullable=false)
	private int leavesTotal;
	@Column(nullable=false)
	private int leavesRemaining;
	@ManyToMany(fetch=FetchType.EAGER)
	private List<EmployeeAdditionalRoles> roles = new ArrayList<EmployeeAdditionalRoles>();
	
	public int getEmpCode() {
		return empCode;
	}
	public void setEmpCode(int empCode) {
		this.empCode = empCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public EmployeeStatus getStatus() {
		return status;
	}
	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public Date getDoj() {
		return doj;
	}
	public void setDoj(Date doj) {
		this.doj = doj;
	}
	public int getLeavesTotal() {
		return leavesTotal;
	}
	public void setLeavesTotal(int leavesTotal) {
		this.leavesTotal = leavesTotal;
	}
	public int getLeavesRemaining() {
		return leavesRemaining;
	}
	public void setLeavesRemaining(int leavesRemaining) {
		this.leavesRemaining = leavesRemaining;
	}
	public List<EmployeeAdditionalRoles> getRoles() {
		return roles;
	}
	public void setRoles(List<EmployeeAdditionalRoles> roles) {
		this.roles = roles;
	}
}
