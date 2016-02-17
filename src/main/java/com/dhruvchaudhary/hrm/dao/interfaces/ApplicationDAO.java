package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.model.Application;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;

public interface ApplicationDAO extends GenericDAO<Application> {
	public List<Application> listPendingApplicationsByManager(Employee manager);
	public List<Application> listApplicationsByEmployee(Employee employee, ApplicationTypes applicationType);
	public boolean checkIfApplicationExists(Employee employee, Date fromDate, Date toDate, ApplicationTypes type);
	public boolean checkIfCompoffApplicationExists(Employee employee, Date date, String field);
	public List<Application> listAcknowledgedApplicationsByManager(Employee manager);
}
