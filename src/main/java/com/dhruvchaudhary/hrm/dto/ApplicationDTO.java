package com.dhruvchaudhary.hrm.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;

public class ApplicationDTO implements Comparable<ApplicationDTO>{

	private String id;
	
	private String empCode;
	
	private String mgrCode;
	
	private String empName;
	
	private String applicationType;
	
	@NotEmpty
	private String fromDate;
	
	@NotEmpty
	private String toDate;
	
	private String appliedOn;
	
	private String applicationStatus;
	
	@NotEmpty
	private String applicationReason;
	
	private String rejectReason;
	
	private String acknowledgedOn;

	private Logger log = Logger.getLogger(ApplicationDTO.class);

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

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAppliedOn() {
		return appliedOn;
	}

	public void setAppliedOn(String appliedOn) {
		this.appliedOn = appliedOn;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getApplicationReason() {
		return applicationReason;
	}

	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getAcknowledgedOn() {
		return acknowledgedOn;
	}

	public void setAcknowledgedOn(String acknowledgedOn) {
		this.acknowledgedOn = acknowledgedOn;
	}

	public String getMgrCode() {
		return mgrCode;
	}

	public void setMgrCode(String mgrCode) {
		this.mgrCode = mgrCode;
	}

	@Override
	public int compareTo(ApplicationDTO o) {
		DateFormat formatter =  new SimpleDateFormat("MMM dd, yyyy");
		int retVal = 0;
		try {
			Date thisDate = formatter.parse(this.appliedOn);
			Date theirDate = formatter.parse(o.getAppliedOn());
			if(thisDate.after(theirDate))
				retVal = -1;
			else if(thisDate.before(theirDate))
				retVal = 1;
			else{
				retVal = -(Integer.parseInt(this.getId())-Integer.parseInt(o.getId()));
			}
		} catch (ParseException e) {
			log .error(e.toString());
			e.printStackTrace();
		}
		return retVal;
	}
	
}
