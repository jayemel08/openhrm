package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;

@Repository
public interface CompensatoryOffDAO extends GenericDAO<CompensatoryOff> {
	public List<CompensatoryOff> getValidUnusedCompensatoryOffs(Employee employee, Date date);
	public List<CompensatoryOff> listAll(int empCode);
	public void cancelLeaveAgainstCompensatoryOff(int leaveId);
	public Map<Integer, String> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate);
	public Map<Integer, String> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate);
	public List<CompensatoryOff> getEmployeeReport(int empCode, Date fromDate, Date toDate);
	public List<CompensatoryOff> listAll(Date fromDate, Date toDate, int empCode);
	public List<CompensatoryOff> getAllUnusedCompOffs(int empCode);
}
