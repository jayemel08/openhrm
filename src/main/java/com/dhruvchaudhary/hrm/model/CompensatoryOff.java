package com.dhruvchaudhary.hrm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "compensatory_off", uniqueConstraints = {@UniqueConstraint(columnNames = {"empCode","appliedAgainst"})})
public class CompensatoryOff {

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "empCode")
	private Employee employee;
	@ManyToOne
	@JoinColumn(name = "applicationId")
	private Application application;
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date appliedAgainst;  //date on which emp has worked extra hrs
	@OneToOne
	@JoinColumn(name = "leaveId")
	private Leave leave;
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
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public Date getAppliedAgainst() {
		return appliedAgainst;
	}
	public void setAppliedAgainst(Date appliedAgainst) {
		this.appliedAgainst = appliedAgainst;
	}
	public Leave getLeave() {
		return leave;
	}
	public void setLeave(Leave leave) {
		this.leave = leave;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
