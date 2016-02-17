package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.utils.LeaveType;

public interface LeaveDAO extends GenericDAO<Leave> {
	public List<Leave> listAll(int empCode, LeaveType type);
	public List<Leave> listAll(int empCode, int applicationID);
	public List<Leave> listAll(Date date);
	public List<Leave> listAll(Date fromDate, Date toDate, int empCode);
	public List<Leave> listAll(Date fromDate, Date toDate, int empCode, LeaveType type);
	public void cancel(int id);
	public Map<Integer, ArrayList<String>> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate);
	public Map<Integer, ArrayList<String>> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate);
	public List<Leave> getEmployeeReport(int empCode, Date fromDate, Date toDate);
}
