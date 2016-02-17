package com.dhruvchaudhary.hrm.service.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;

@Service
public interface ManagerService {
	public List<ApplicationDTO> listAllPendingApplications();
	public void approveApplication(int id) throws InvalidRequestException, MessagingException;
	public List<EmployeeDTO> listTeam(int mgrCode);
	public List<EmployeeDTO> listEmployeesOnLeaveToday(Date date, int mgrCode);
	public List<EmployeeDTO> listEmployeesOnWFHToday(Date date, int mgrCode);
	public void rejectApplication(int appId, String reason) throws InvalidRequestException, MessagingException;
	public List<ApplicationDTO> listAllAcknowledgedApplications();
	
	public Map<Integer, ArrayList<String>> generateConsolidatedReport(int mgrCode, Date fromDate, Date toDate);
	public Map<String, Object> generateEmployeeWiseReport(int mgrCode, int empCode);
}
