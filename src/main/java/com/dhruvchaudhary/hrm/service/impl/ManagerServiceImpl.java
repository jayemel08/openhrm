package com.dhruvchaudhary.hrm.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.AuthorizationFaliureException;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.exceptions.NoSuchElementException;
import com.dhruvchaudhary.hrm.service.interfaces.ManagerService;
import com.dhruvchaudhary.hrm.utils.ApplicationStatus;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;
import com.dhruvchaudhary.hrm.utils.LeaveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhruvchaudhary.hrm.dao.interfaces.ApplicationDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.EntitiesPendingApprovalDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.HolidayDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.CompensatoryOffDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.LeaveDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.WorkFromHomeDAO;
import com.dhruvchaudhary.hrm.model.Application;
import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.WorkFromHome;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private EmployeeDAO employeeDAOImpl;
	
	@Autowired
	private ApplicationDAO applicationDAOImpl;

	@Autowired
	private LeaveDAO leaveDAOImpl;
	
	@Autowired
	private CompensatoryOffDAO compensatoryOffDAOImpl;
	
	@Autowired
	private WorkFromHomeDAO workFromHomeDAOImpl;
	
	@Autowired
	private HolidayDAO calenderLeaveDAOImpl;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private EntitiesPendingApprovalDAO entitiesPendingApprovalDAO;

	@Autowired
	private MessageSource messageSource;
	
	@Override
	@Transactional(readOnly = true)
	public List<ApplicationDTO> listAllPendingApplications() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
		List<ApplicationDTO> retList = new ArrayList<ApplicationDTO>();
		for(Application app:applicationDAOImpl.listPendingApplicationsByManager(employeeDAOImpl.findById(empCode)))
			retList.add(applicationEntitiyToApplicationDTO(app));
		return retList;
	}

	private ApplicationDTO applicationEntitiyToApplicationDTO(Application entity) {
		ApplicationDTO retDTO = new ApplicationDTO();
		retDTO.setApplicationReason(entity.getApplicationReason());
		retDTO.setApplicationStatus(entity.getStatus().getDescription());
		retDTO.setApplicationType(entity.getType().getDescription());

		DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		retDTO.setAppliedOn(formatter.format(entity.getAppliedOn()).toString());

		retDTO.setEmpCode(Integer.toString(entity.getEmployee().getEmpCode()));
		retDTO.setEmpName(entity.getEmployee().getName());

		retDTO.setFromDate(formatter.format(entity.getFromDate()).toString());
		retDTO.setId(Integer.toString(entity.getId()));
		retDTO.setMgrCode(Integer.toString(entity.getManager().getEmpCode()));
		retDTO.setToDate(formatter.format(entity.getToDate()).toString());
		return retDTO;
	}

	@Override
	@Transactional
	public void approveApplication(int id) throws InvalidRequestException, MessagingException {
		Application applicationToApprove = applicationDAOImpl.findById(id);
		if(applicationToApprove != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
			if(applicationToApprove.getManager().getEmpCode() != empCode)
				throw new AuthorizationFaliureException();
			else if(!(applicationToApprove.getStatus() == ApplicationStatus.PENDING))
				throw new InvalidRequestException("This application is already acknowledged");
			else {
				Calendar c = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				to.setTime(applicationToApprove.getToDate());
				to.add(Calendar.DATE, 1);
				switch (applicationToApprove.getType()) {
				/*case "Work From Home":*/
				case WORK_FROM_HOME:
					for(c.setTime(applicationToApprove.getFromDate()); c.before(to); c.add(Calendar.DATE, 1)){
						WorkFromHome wfh = new WorkFromHome();
						wfh.setEmployee(applicationToApprove.getEmployee());
						wfh.setApplication(applicationToApprove);
						wfh.setDate(c.getTime());
						workFromHomeDAOImpl.add(wfh);
					}
					break;

				/*case "Compensatory Off":*/
				case COMPENSATORY_OFF:
					for(c.setTime(applicationToApprove.getFromDate()); c.before(to); c.add(Calendar.DATE, 1)){
						CompensatoryOff compensatoryOff = new CompensatoryOff();
						compensatoryOff.setEmployee(applicationToApprove.getEmployee());
						compensatoryOff.setApplication(applicationToApprove);
						compensatoryOff.setAppliedAgainst(c.getTime());
						compensatoryOffDAOImpl.add(compensatoryOff);
					}
					break;

				/*case "Leave":*/
				case LEAVE:
					Employee emp = applicationToApprove.getEmployee();
					for(c.setTime(applicationToApprove.getFromDate()); c.before(to); c.add(Calendar.DATE, 1)){
						if(!((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (calenderLeaveDAOImpl.findByDate(c.getTime()) != null))) {
							Leave leave = new Leave();
							leave.setApplication(applicationToApprove);
							leave.setEmployee(applicationToApprove.getEmployee());
							leave.setDate(c.getTime());
							if(emp.getLeavesRemaining() > 0 && emp.getStatus() != EmployeeStatus.IN_NOTICE_PERIOD) {
								emp.setLeavesRemaining(emp.getLeavesRemaining() - 1);
								leave.setLeaveType(LeaveType.LEAVE_PAID);
							}
							else 
								leave.setLeaveType(LeaveType.LEAVE_WITHOUT_PAY);
							leaveDAOImpl.add(leave);
							System.out.println(c.getTime().toString());
						}
					}
					employeeDAOImpl.edit(emp);
					break;

				/*case "Leave Against Compensatory Off":*/
				case LEAVE_AGAINST_COMPENSATORY_OFF:
					for(c.setTime(applicationToApprove.getFromDate()); c.before(to); c.add(Calendar.DATE, 1)){
						if(!((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (calenderLeaveDAOImpl.findByDate(c.getTime()) != null))) {
							Leave leave = new Leave();
							leave.setApplication(applicationToApprove);
							leave.setEmployee(applicationToApprove.getEmployee());
							leave.setDate(c.getTime());
							//applicationToApprove.getEmployee().setLeavesRemaining(applicationToApprove.getEmployee().getLeavesRemaining() - 1);
							leave.setLeaveType(LeaveType.LEAVE_AGAINST_COMP_OFF);
							leaveDAOImpl.add(leave);
							CompensatoryOff compOff = compensatoryOffDAOImpl.getValidUnusedCompensatoryOffs(applicationToApprove.getEmployee(), c.getTime()).get(0);
							compOff.setLeave(leave);
							compensatoryOffDAOImpl.edit(compOff);
						}
					}
					break;

				}
				applicationToApprove.setAcknowledgedOn(new Date());
				applicationToApprove.setStatus(ApplicationStatus.APPROVED);
				applicationDAOImpl.edit(applicationToApprove);
				
				//Delete from EntitiesPendingApproval
				entitiesPendingApprovalDAO.deleteAllByApplication(applicationToApprove.getId());
				
				//Mail to Employee and CC to HR
				String signature = messageSource.getMessage("email-signature", null, Locale.US);
				String applicationName = messageSource.getMessage("application-name", null, Locale.US);
				
				MimeMessage msg = mailSender.createMimeMessage();
				MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
				
				/*SimpleMailMessage email = new SimpleMailMessage();*/
				Employee employee = applicationToApprove.getEmployee();
		        email.setTo(employee.getEmail());
		        email.setCc(messageSource.getMessage("hr-email", null, Locale.US)); // TODO: set this to nisha
		        email.setSubject(applicationName + " | Application (ID: " + applicationToApprove.getId() + ") Approved");
		        String message = "<html><body>" + "Dear <b>" + employee.getName() + "</b>,<br /><br />";
		        message = message + "Your application (ID: " + applicationToApprove.getId() +
		        		") has been approved by your manager.<br /><br />" +
						signature + 
						"</body></html>";
		        email.setText("", message);
		        mailSender.send(msg);
			}
		}
		else {
			throw new NoSuchElementException("Application does not exist");
		}
	}

	@Override
	@Transactional
	public void rejectApplication(int appId, String reason) throws InvalidRequestException, MessagingException {
		Application applicationToReject = applicationDAOImpl.findById(appId);
		if(applicationToReject != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
			if(applicationToReject.getManager().getEmpCode() != empCode)
				throw new AuthorizationFaliureException();
			else if(!(applicationToReject.getStatus() == ApplicationStatus.PENDING))
				throw new InvalidRequestException("This application is already acknowledged.");
			else {
				applicationToReject.setAcknowledgedOn(new Date());
				applicationToReject.setRejectionReason(reason);
				applicationToReject.setStatus(ApplicationStatus.REJECTED);
				applicationDAOImpl.edit(applicationToReject);
				
				//Delete from EntitiesPendingApproval
				entitiesPendingApprovalDAO.deleteAllByApplication(applicationToReject.getId());
								
				//Send mail to Employee

				String signature = messageSource.getMessage("email-signature", null, Locale.US);
				String applicationName = messageSource.getMessage("application-name", null, Locale.US);
				
				MimeMessage msg = mailSender.createMimeMessage();
				MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
				
				/*SimpleMailMessage email = new SimpleMailMessage();*/
				Employee employee = applicationToReject.getEmployee();
		        email.setTo(employee.getEmail());
		        email.setSubject(applicationName + " | Application (ID: " + applicationToReject.getId() + ") Rejected");
		        String message = "<html><body>" + "Dear <b>" + employee.getName() + "</b>,<br /><br />";
		        message = message + "Your application (ID: " + applicationToReject.getId() +
		        		") has been rejected by your manager.<br /><br />" +
		        		"Rejection reason: " + applicationToReject.getRejectionReason() + "<br /><br />" +
						signature + 
						"</body></html>";
		        email.setText("", message);
		        mailSender.send(msg);
			}
		}
		else {
			throw new NoSuchElementException("Application does not exist");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ApplicationDTO> listAllAcknowledgedApplications() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
		List<ApplicationDTO> retList = new ArrayList<ApplicationDTO>();
		for(Application app:applicationDAOImpl.listAcknowledgedApplicationsByManager(employeeDAOImpl.findById(empCode)))
			retList.add(applicationEntitiyToApplicationDTO(app));
		return retList;
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listTeam(int mgrCode) {
		List<EmployeeDTO> listOfEmployeesInTeam = new ArrayList<EmployeeDTO>();
		for(Employee employee:employeeDAOImpl.listAll()) {
			if(employee.getManager() != null && employee.getManager().getEmpCode() == mgrCode)
				listOfEmployeesInTeam.add(employeeEntityToDTO(employee));
		}
	
		return listOfEmployeesInTeam;
	}
	
	private EmployeeDTO employeeEntityToDTO(Employee employee) {
		EmployeeDTO retEmployee = new EmployeeDTO();
		retEmployee.setEmpCode(Integer.toString(employee.getEmpCode()));
		retEmployee.setEmail(employee.getEmail());
		retEmployee.setName(employee.getName());
		return retEmployee;
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listEmployeesOnLeaveToday(Date date, int mgrCode) {
		List<EmployeeDTO> listOfEmployeesInTeam = new ArrayList<EmployeeDTO>();
		for(Leave leave:leaveDAOImpl.listAll(new Date())) {
			if(leave.getEmployee().getManager() != null && leave.getEmployee().getManager().getEmpCode() == mgrCode)
				listOfEmployeesInTeam.add(employeeEntityToDTO(leave.getEmployee()));
		}
		return listOfEmployeesInTeam;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listEmployeesOnWFHToday(Date date, int mgrCode) {
		List<EmployeeDTO> listOfEmployeesInTeam = new ArrayList<EmployeeDTO>();
		for(WorkFromHome wfh:workFromHomeDAOImpl.listAll(new Date())) {
			if(wfh.getEmployee().getManager() != null && wfh.getEmployee().getManager().getEmpCode() == mgrCode)
				listOfEmployeesInTeam.add(employeeEntityToDTO(wfh.getEmployee()));
		}
		return listOfEmployeesInTeam;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, ArrayList<String>> generateConsolidatedReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, ArrayList<String>> retMap = employeeDAOImpl.listAllByManager(mgrCode); 
		Map<Integer, ArrayList<String>> leaveMap = leaveDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		Map<Integer, String> coMap = compensatoryOffDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		Map<Integer, String> wfhMap = workFromHomeDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		for(Entry<Integer, ArrayList<String>> e: leaveMap.entrySet()) {
			if(retMap.containsKey(e.getKey())) { 
				retMap.get(e.getKey()).set(1, e.getValue().get(0));
				retMap.get(e.getKey()).set(2, e.getValue().get(1));
				retMap.get(e.getKey()).set(3, e.getValue().get(2));
			}
		}
		for(Entry<Integer, String> e: coMap.entrySet()) {
			if(retMap.containsKey(e.getKey()))
				retMap.get(e.getKey()).set(4, e.getValue());
		}
		for(Entry<Integer, String> e: wfhMap.entrySet()) {
			if(retMap.containsKey(e.getKey()))
				retMap.get(e.getKey()).set(5, e.getValue());
		}
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> generateEmployeeWiseReport(int mgrCode, int empCode) {
		if(employeeDAOImpl.findById(empCode).getManager().getEmpCode() != mgrCode)
			return null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("Employee", employeeEntityToDTO(employeeDAOImpl.findById(empCode)));
		retMap.put("L", leaveDAOImpl.listAll(empCode, LeaveType.LEAVE_PAID));
		retMap.put("LACO", leaveDAOImpl.listAll(empCode, LeaveType.LEAVE_AGAINST_COMP_OFF));
		retMap.put("LWP", leaveDAOImpl.listAll(empCode, LeaveType.LEAVE_WITHOUT_PAY));
		retMap.put("WFH", workFromHomeDAOImpl.listAll(empCode));
		retMap.put("C", compensatoryOffDAOImpl.listAll(empCode));
		return retMap;
	}
	
}
