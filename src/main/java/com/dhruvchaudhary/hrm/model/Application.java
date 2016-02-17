package com.dhruvchaudhary.hrm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dhruvchaudhary.hrm.utils.ApplicationStatus;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;

@Entity
@Table(name = "application")
public class Application {

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "empCode")
	private Employee employee;
	@ManyToOne
	@JoinColumn(name = "mgrCode")
	private Employee manager;
	/*@ManyToOne
	@JoinColumn(name = "type")
	private ApplicationType type;*/
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private ApplicationTypes type;
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date fromDate;
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date toDate;
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date appliedOn;
	/*@ManyToOne
	@JoinColumn(name = "status")
	private ApplicationStatus status;*/
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private ApplicationStatus status;
	@Lob
	@Column(nullable=false)
	private String applicationReason;
	@Lob
	private String rejectionReason;
	@Temporal(TemporalType.DATE)
	private Date acknowledgedOn;
	
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
	/*public ApplicationType getType() {
		return type;
	}
	public void setType(ApplicationType type) {
		this.type = type;
	}*/
	public Date getFromDate() {
		return fromDate;
	}
	public ApplicationTypes getType() {
		return type;
	}
	public void setType(ApplicationTypes type) {
		this.type = type;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date todate) {
		this.toDate = todate;
	}
	public Date getAppliedOn() {
		return appliedOn;
	}
	public void setAppliedOn(Date appliedOn) {
		this.appliedOn = appliedOn;
	}
	/*public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}*/
	public String getApplicationReason() {
		return applicationReason;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public Date getAcknowledgedOn() {
		return acknowledgedOn;
	}
	public void setAcknowledgedOn(Date acknowledgedOn) {
		this.acknowledgedOn = acknowledgedOn;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
}
