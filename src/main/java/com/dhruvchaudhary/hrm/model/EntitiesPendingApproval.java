package com.dhruvchaudhary.hrm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "entities_pending_approval")
public class EntitiesPendingApproval {
	
	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "empCode")
	private Employee employee;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date;
	@ManyToOne
	@JoinColumn(name = "applicationId")
	private Application application;
	
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
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
}
