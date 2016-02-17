package com.dhruvchaudhary.hrm.service.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dhruvchaudhary.hrm.dto.HolidayDTO;
import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.WorkFromHome;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;

@Service
public interface HRService {
	public void addEmployee(EmployeeDTO newEmployee) throws DataAccessException, MessagingException;
	public void editEmployee(EmployeeDTO updatedEmployee);
	public void deleteEmployee(int empCode);
	public EmployeeDTO getEmployeeByEmployeeCode(int empCode);
	public List<EmployeeDTO> listAllEmployees();
	public List<EmployeeDTO> listAllEmployees(String criteria);
	public void addCalenderLeaves(HolidayDTO newCalenderLeave);
	public void deleteCalenderLeaves(int id);
	public List<HolidayDTO> listAllCalenderLeaves();
	public List<EmployeeAdditionalRoles> listAllAdditionalRoles();
	public int getEmployeeCount(EmployeeStatus status);
	public List<EmployeeDTO> listEmployeesOnLeave(Date date);
	public List<EmployeeDTO> listEmployeesOnWFH(Date date);
	public Map<Integer, ArrayList<String>> generateConsolidatedReport(Date fromDate, Date toDate);
	public Map<Integer, ArrayList<String>> generateManagerWiseReport(int mgrCode, Date fromDate, Date toDate);
	public ArrayList<ArrayList<String>> generateEmployeeWiseReport(ApplicationTypes type, int empCode, Date fromDate, Date toDate);
	public Map<Employee, ArrayList<Leave>> getLeaves();
	public Map<Employee, ArrayList<CompensatoryOff>> getCompOffs();
	public Map<Employee, ArrayList<WorkFromHome>> getWFH();
	public List<EmployeeDTO> listAllManagers();
	public ArrayList<ArrayList<String>> getEmployeeApplicationData(int empCode, int applicationId);
	public void cancel(int id, int type) throws InvalidRequestException;
	public List<Leave> getEmployeeLeaveReport(int empCode, Date fromDate, Date toDate);
	public List<WorkFromHome> getEmployeeWFHReport(int empCode, Date fromDate, Date toDate);
	public List<CompensatoryOff> getEmployeeCompOffReport(int empCode, Date fromDate, Date toDate);
}
