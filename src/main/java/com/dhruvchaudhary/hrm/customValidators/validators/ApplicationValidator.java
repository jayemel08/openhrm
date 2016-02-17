package com.dhruvchaudhary.hrm.customValidators.validators;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.dao.interfaces.ApplicationDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.CompensatoryOffDAO;
import com.dhruvchaudhary.hrm.model.EntitiesPendingApproval;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.EntitiesPendingApprovalDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.HolidayDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.LeaveDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.WorkFromHomeDAO;
import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;
import com.dhruvchaudhary.hrm.utils.LeaveType;
import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;

@Component
public class ApplicationValidator implements Validator {
	
	@Autowired
	private ApplicationDAO applicationDAOImpl;
	
	@Autowired
	private EmployeeDAO employeeDAOImpl;
	
	@Autowired
	private CompensatoryOffDAO compensatoryOffDAO;
	
	@Autowired
	private LeaveDAO leaveDAO;
	
	@Autowired
	private WorkFromHomeDAO wfhDAO;
	
	@Autowired
	private HolidayDAO holidayDAOImpl;
	
	@Autowired
	private EntitiesPendingApprovalDAO entitiesPendingApprovalDAO;
/*	
	@Autowired
	private SessionFactory sessionFactory;*/

	@Override
	public boolean supports(Class<?> arg0) {
		return ApplicationDTO.class.equals(arg0);
	}

	@Override
	public void validate(Object object, Errors errors) {
		/*sessionFactory.openSession();*/
		ApplicationDTO application = (ApplicationDTO) object;
		
		//Application Reason Required
		if(application.getApplicationReason().trim().length() == 0) {
			errors.rejectValue("applicationReason", "applicationReason", "Application reason may not be empty");
		}
		
		
		//Date is in proper format.
		DateFormat formatter = new SimpleDateFormat("EEEEE, dd MMMMM, yyyy");
		Date fromDate = null;
		Date toDate = null;
		try {
			if(application.getFromDate().trim().length() > 0)
				fromDate = formatter.parse(application.getFromDate());
			else
				errors.rejectValue("fromDate", "fromDate", "\"From\" date may not be empty.");
		} catch (ParseException e) {
			errors.rejectValue("fromDate", "fromDate", "Invalid format of \"From\" date");
		}
		
		try {
			if((application.getToDate().trim().length() > 0))
				toDate = formatter.parse(application.getToDate());
			else
				errors.rejectValue("toDate", "toDate", "\"To\" date may not be empty.");
		}catch(ParseException e) {
			errors.rejectValue("toDate", "toDate", "Invalid format of \"To\" date");
		}
		
		if(fromDate == null || toDate == null)
			return;
		
		//from date is <= to date
		if(fromDate.after(toDate))
		{
			errors.rejectValue("fromDate", "fromDate", "\"From\" Date must be less than or equal to \"To\" Date.");
			
		}
		
		if(errors.hasErrors())
			return;
		
		Employee employee = employeeDAOImpl.findById(Integer.parseInt(application.getEmpCode()));
		if(employee.getManager() == null) {
			errors.rejectValue("fromDate", "fromDate", "Since you do not have a manager, you cannot submit an application");
			return;
		}
		switch(ApplicationTypes.values()[Integer.parseInt(application.getApplicationType())]) {
		case LEAVE:
			validateLeave(employee, fromDate, toDate, Integer.parseInt(application.getEmpCode()), errors);
			break;
		case LEAVE_AGAINST_COMPENSATORY_OFF:
			validateLeaveAgainstCompOff(employee, fromDate, toDate,Integer.parseInt(application.getEmpCode()), errors);
			break;
		case WORK_FROM_HOME:
			validateWFH(employee, fromDate, toDate,Integer.parseInt(application.getEmpCode()), errors);
			break;
		case COMPENSATORY_OFF:
			validateCompOff(employee, fromDate, toDate,Integer.parseInt(application.getEmpCode()), errors);
			break;
		default:
			break;
		}
	}
	
	private void validateCompOff(Employee employee, Date fromDate, Date toDate, int empCode, Errors errors) {
		if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.COMPENSATORY_OFF).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Compensatory Off(s)\" pending approval in the given period.");
			return;
		} 
		else if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.LEAVE).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) pending approval in the given period.");
			return;
		}
		else if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) pending approval in the given period.");
			return;
		}
		else if(leaveDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) in the given period"); 
			return;
		}
		else if(compensatoryOffDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Compensatory Off(s)\" granted against days in the given period");
			return;
		}
	}
	
	private void validateLeave(Employee employee, Date fromDate, Date toDate, int empCode, Errors errors) {
		/*sessionFactory.openSession();*/
		/*Employee employee = employeeDAOImpl.findById(empCode);*/
		/*if(employee.getStatus() == EmployeeStatus.IN_NOTICE_PERIOD) {
			errors.rejectValue("fromDate", "fromDate", "You are not eligible for leaves as you are in notice period.");
			return;
		}*/
		Calendar c = Calendar.getInstance();
		c.setTime(fromDate);
		if((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (holidayDAOImpl.findByDate(c.getTime()) != null)) {
			errors.rejectValue("fromDate", "fromDate", "\"From\" date collides with a holiday");
			return;
		}
		
		c.setTime(toDate);
		if((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (holidayDAOImpl.findByDate(c.getTime()) != null)) {
			errors.rejectValue("toDate", "toDate", "\"To\" date collides with a holiday");
			return;
		}
		
		if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), null).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing application(s) pending approval in the given period.");
			return;
		}
		else if(leaveDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) in the given period"); 
			return;
		}
		else if(wfhDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Work from home\" in the given period"); 
			return;
		}
		else if(compensatoryOffDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Compensatory Off(s)\" granted against days in the given period"); 
			return;
		}
	}
	
	private void validateLeaveAgainstCompOff(Employee employee, Date fromDate, Date toDate, int empCode, Errors errors) {
		Calendar c = Calendar.getInstance();
		c.setTime(fromDate);
		if((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (holidayDAOImpl.findByDate(c.getTime()) != null)) {
			errors.rejectValue("fromDate", "fromDate", "\"From\" date collides with a holiday");
			return;
		}
		
		c.setTime(toDate);
		if((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (holidayDAOImpl.findByDate(c.getTime()) != null)) {
			errors.rejectValue("toDate", "toDate", "\"To\" date collides with a holiday");
			return;
		}
		
		try {
			long from = fromDate.getTime();
			long to = toDate.getTime();
			long diff = ((to-from)/86400000) + 1;
			
			if(diff>2) { //>1 cuz date is counted on 00:00 Hrs, hence 2 days give difference of 1 day.
				errors.rejectValue("applicationReason", "applicationReason", "Leave against compensatory off can't be applied for more that two days consecutively.");
				return;
			}
			
			if(employee.getStatus() == EmployeeStatus.IN_NOTICE_PERIOD) {
				errors.rejectValue("fromDate", "fromDate", "You are not eligible for leaves as you are in notice period.");
				return;
			}
			
			if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), null).size() > 0) {
				errors.rejectValue("fromDate", "fromDate", "You already have existing application(s) pending approval in the given period.");
				return;
			}
			else if(leaveDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
				errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) in the given period"); 
				return;
			}
			else if(wfhDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
				errors.rejectValue("fromDate", "fromDate", "You already have existing \"Work from home\" in the given period"); 
				return;
			}
			else if(compensatoryOffDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
				errors.rejectValue("fromDate", "fromDate", "You already have existing \"Compensatory Off(s)\" granted against days in the given period");
				return;
			}
			
			//Pending LACO check with valid compoff balance
			List<CompensatoryOff> listOfValidUnusedCompensatoryOffs = compensatoryOffDAO.getAllUnusedCompOffs(empCode);
			List<EntitiesPendingApproval> listOfEntitiesPendingApproval = entitiesPendingApprovalDAO.listAll(empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF);
			
			
			Calendar iterator = Calendar.getInstance();
			iterator.setTime(fromDate);
			
			Calendar toCal = Calendar.getInstance();
			toCal.setTime(toDate);
			toCal.add(Calendar.DATE, 1);
			for(; iterator.before(toCal); iterator.add(Calendar.DATE, 1)) {	
				EntitiesPendingApproval e = new EntitiesPendingApproval();
				e.setDate(iterator.getTime());
				listOfEntitiesPendingApproval.add(e);
			}
			
			for(EntitiesPendingApproval e:listOfEntitiesPendingApproval) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(e.getDate());
				cal.add(Calendar.DATE, -90);
				int i = 0;
				boolean isFound = false;
				if(listOfValidUnusedCompensatoryOffs.size() > 0) {
					for(; i<listOfValidUnusedCompensatoryOffs.size(); i++) {
						if(listOfValidUnusedCompensatoryOffs.get(i).getAppliedAgainst().equals(cal.getTime()) || listOfValidUnusedCompensatoryOffs.get(i).getAppliedAgainst().after(cal.getTime())) {
							listOfValidUnusedCompensatoryOffs.remove(i);
							isFound = true;
							break;
						}
					}
					if(!isFound) {
						errors.rejectValue("applicationReason", "applicationReason", "You do not have enough compensatory offs available.");
						return;
					}
				}
				else {
					errors.rejectValue("applicationReason", "applicationReason", "You do not have enough compensatory offs available.");
					return;
				}
			}
			
			ArrayList<Date> datesToBeChecked = new ArrayList<Date>();
			datesToBeChecked.add(fromDate);
			if(!datesToBeChecked.contains(toDate))
				datesToBeChecked.add(toDate);
			
			iterator.setTime(fromDate);
			for(int i = 0; i<2; i++) {
				iterator.add(Calendar.DATE, -1);
				if((iterator.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (iterator.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
					i--;
				else
					datesToBeChecked.add(iterator.getTime());
			}
			
			iterator.setTime(toDate);
			for(int i = 0; i<2; i++) {
				iterator.add(Calendar.DATE, 1);
				if((iterator.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (iterator.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
					i--;
				else
					datesToBeChecked.add(iterator.getTime());
			}
			
			Collections.sort(datesToBeChecked);

			int countOfConsecutiveCompensatoryOffs = 0;
			for(Date d: datesToBeChecked) {
				boolean isFound = false;
				for(EntitiesPendingApproval e:listOfEntitiesPendingApproval) {
					if(e.getDate().equals(d)) {
						isFound = true;
						break;
					}
				}
				if(isFound)
					countOfConsecutiveCompensatoryOffs++;
				else if(leaveDAO.listAll(d, d, empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)
					countOfConsecutiveCompensatoryOffs++;
				else 
					countOfConsecutiveCompensatoryOffs = 0;
				if(countOfConsecutiveCompensatoryOffs > 2)
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
			}
			
			/*Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);
			
			toCal = Calendar.getInstance();
			toCal.setTime(toDate);
			switch((int)diff) {
			case 1:
				do {
					fromCal.add(Calendar.DATE, -1);
				} while((fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
				do {
					toCal.add(Calendar.DATE, 1);		
				} while((toCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (toCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
				
				if((entitiesPendingApprovalDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) &&
						(entitiesPendingApprovalDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0)) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				
				if((leaveDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0) && 
						(leaveDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				
				Calendar from_1 = Calendar.getInstance();
				from_1.setTime(fromCal.getTime());
				from_1.add(Calendar.DATE, -1);
				
				if(((entitiesPendingApprovalDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) ||
						(leaveDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)) &&
						((entitiesPendingApprovalDAO.listAll(from_1.getTime(), from_1.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) ||
						(leaveDAO.listAll(from_1.getTime(), from_1.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0))) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				
				Calendar to_1 = Calendar.getInstance();
				to_1.setTime(toCal.getTime());
				to_1.add(Calendar.DATE, 1);
				
				if(((entitiesPendingApprovalDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) ||
						(leaveDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)) &&
						((entitiesPendingApprovalDAO.listAll(to_1.getTime(), to_1.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) ||
						(leaveDAO.listAll(to_1.getTime(), to_1.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0))) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				Calendar from_1 = fromCal;
				from_1.add(Calendar.DATE, -1);
				Calendar to_1 = toCal;
				to_1.add(Calendar.DATE, 1);
				
				if((entitiesPendingApprovalDAO.listAll(from_1.getTime(), fromCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 1)
						|| (entitiesPendingApprovalDAO.listAll(toCal.getTime(), to_1.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 1)
						|| (compensatoryOffDAO.listAll(from_1.getTime(), fromCal.getTime(), empCode).size() > 1)
						|| (compensatoryOffDAO.listAll(toCal.getTime(), to_1.getTime(), empCode).size() > 1)) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				
				break;
			case 2:
				do {
					fromCal.add(Calendar.DATE, -1);
				} while((fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
				do {
					toCal.add(Calendar.DATE, 1);		
				} while((toCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (toCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
				if((entitiesPendingApprovalDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0)
						|| (entitiesPendingApprovalDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0)
						|| (leaveDAO.listAll(fromCal.getTime(), fromCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)
						|| (leaveDAO.listAll(toCal.getTime(), toCal.getTime(), empCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size() > 0)) {
					errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
					return;
				}
				break;
			default:
				break;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.add(Calendar.DATE, -1);
			
			//fromDate - 1 
			if(applicationDAOImpl.checkIfCompoffApplicationExists(employee, cal.getTime(), "toDate")) {
				errors.rejectValue("fromDate", "fromDate", "Compensatory Off can't be applied for more that two days consecutively.");
			}
								
			//toDate + 1
			cal.setTime(toDate);
			cal.add(Calendar.DATE, 1);
			if(applicationDAOImpl.checkIfCompoffApplicationExists(employee, cal.getTime(), "fromDate")) {
				errors.rejectValue("applicationReason", "applicationReason", "Compensatory Off can't be applied for more that two days consecutively.");
			}*/
			
			
		}catch(NumberFormatException e) {
			errors.rejectValue("applicationReason", "applicationReason", "Unknown Error");
		}
	}
	
	private void validateWFH(Employee employee, Date fromDate, Date toDate, int empCode, Errors errors) { 
		if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.LEAVE).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) pending approval in the given period.");
			return;
		}
		else if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) pending approval in the given period.");
			return;
		}
		else if(entitiesPendingApprovalDAO.listAll(fromDate, toDate, employee.getEmpCode(), ApplicationTypes.WORK_FROM_HOME).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Work from home\" pending approval in the given period.");
			return;
		}
		else if(leaveDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing leave(s) in the given period"); 
			return;
		}
		else if(wfhDAO.listAll(fromDate, toDate, employee.getEmpCode()).size() > 0) {
			errors.rejectValue("fromDate", "fromDate", "You already have existing \"Work from home\" in the given period"); 
			return;
		}
	}
}
