package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.model.WorkFromHome;

public interface WorkFromHomeDAO extends GenericDAO<WorkFromHome> {
	public List<WorkFromHome> listAll(Date date);
	public List<WorkFromHome> listAll(int empCode);
	public List<WorkFromHome> listAll(int empCode, int applicationId);
	public void cancel(int id);
	public Map<Integer, String> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate);
	public Map<Integer, String> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate);
	public List<WorkFromHome> getEmployeeReport(int empCode, Date fromDate, Date toDate);
	public List<WorkFromHome> listAll(Date fromDate, Date toDate, int empCode);
}
