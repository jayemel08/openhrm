package com.dhruvchaudhary.hrm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.dhruvchaudhary.hrm.utils.LeaveType;

@Entity
@Table(name = "leaves", uniqueConstraints = {@UniqueConstraint(columnNames = {"empCode", "date"})})
public class Leave {
	
	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "empCode")
	private Employee employee;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date;
	/*@ManyToOne
	@JoinColumn(name = "type")
	private LeaveType leaveType;*/
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private LeaveType leaveType;
	@Temporal(TemporalType.DATE)
	private Date cancelDate;
	@ManyToOne
	@JoinColumn(name = "applicationId")
	private Application application;
	@Column(nullable = false, name = "active")
	private boolean active;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	
}
