package com.dhruvchaudhary.hrm.service.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

import com.dhruvchaudhary.hrm.dto.HolidayDTO;
import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.exceptions.NoSuchElementException;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;

@Service
public interface EmployeeService {	
	public boolean changePassword(int empCode, String oldPassword, String newPassword);	
	public List<HolidayDTO> listUpcomingHolidays();	
	public void newApplication(ApplicationDTO newApplication) throws MessagingException;	
	public List<ApplicationDTO> listAllApplications(int empCode, ApplicationTypes applicationType);
	public void deleteApplication(int id) throws InvalidRequestException;
	public int getCompensatoryOffBalance(int empCode);
	public void forgotPassword(int empCode) throws MessagingException, NoSuchElementException;
	public boolean resetPassword(String authCode) throws MessagingException;
	public EmployeeDTO getEmployee(int empCode);
	public ArrayList<ArrayList<String>> getReport(ApplicationTypes type, int empCode, Date fromDate, Date toDate);
}
