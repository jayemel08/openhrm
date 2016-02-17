package com.dhruvchaudhary.hrm.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.dhruvchaudhary.hrm.exceptions.AuthorizationFaliureException;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.service.interfaces.EmployeeService;
import com.dhruvchaudhary.hrm.utils.ApplicationStatus;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.dhruvchaudhary.hrm.dao.interfaces.PasswordResetDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.WorkFromHomeDAO;
import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.dto.HolidayDTO;
import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.NoSuchElementException;
import com.dhruvchaudhary.hrm.model.Application;
import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.EntitiesPendingApproval;
import com.dhruvchaudhary.hrm.model.Holiday;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.PasswordReset;
import com.dhruvchaudhary.hrm.model.WorkFromHome;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private HolidayDAO calenderLeavesDAO;

	@Autowired
	@Qualifier("passwordResetDAOImpl")
	private PasswordResetDAO passwordResetDAO;

	@Autowired
	private ApplicationDAO applicationDAO;

	@Autowired
	private CompensatoryOffDAO compensatoryOffDAO;
	
	@Autowired
	private LeaveDAO leaveDAO;
	
	@Autowired
	private WorkFromHomeDAO workFromHomeDAO;
	
	@Autowired
	private EntitiesPendingApprovalDAO entitiesPendingApprovalDAO;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JavaMailSender mailSender;

	private Logger log = Logger.getLogger(EmployeeServiceImpl.class);

	@Override
	@Transactional
	public boolean changePassword(int empCode, String oldPassword, String newPassword) {
		if(!(DigestUtils.sha1Hex(oldPassword).equals(employeeDAO.findById(empCode).getPassword()))) {
			return false;
		}
		else{
			employeeDAO.changePassword(empCode, DigestUtils.sha1Hex(newPassword));
			return true;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<HolidayDTO> listUpcomingHolidays() {
		List<HolidayDTO> listOfHolidays = new ArrayList<HolidayDTO>();
		for(Holiday holiday:calenderLeavesDAO.listHolidays(new Date())){
			listOfHolidays.add(holidayEntityToDTO(holiday));
		}
		return listOfHolidays;
	}

/*	private Employee employeeDTOToEntity(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		Employee origEmployee = employeeDAO.findById(Integer.parseInt(employeeDTO.getEmpCode()));
		employee.setEmpCode(Integer.parseInt(employeeDTO.getEmpCode()));
		employee.setEmail(origEmployee.getEmail());
		employee.setName(origEmployee.getName());
		employee.setPassword(DigestUtils.sha1Hex(employeeDTO.getNewPassword()));
		employee.setStatus(origEmployee.getStatus());
		employee.setManager(origEmployee.getManager());
		employee.setDoj(origEmployee.getDoj());
		employee.setLeavesTotal(employee.getLeavesTotal());
		employee.setLeavesRemaining(origEmployee.getLeavesRemaining());
		employee.setRoles(origEmployee.getRoles());
		return employee;
	}*/

	private EmployeeDTO employeeEntityToDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmpCode(Integer.toString(employee.getEmpCode()));
		employeeDTO.setEmail(employee.getEmail());
		employeeDTO.setName(employee.getName());

		employeeDTO.setStatus(employee.getStatus().getDescription());
		if(employee.getManager() != null) {
			employeeDTO.setMgrName(employee.getManager().getName());
		}
		else
			employeeDTO.setMgrName("&nbsp;");
		employeeDTO.setDoj(employee.getDoj().toString());
		employeeDTO.setLeavesTotal(Integer.toString(employee.getLeavesTotal()));
		employeeDTO.setLeavesRemaining(Integer.toString(employee.getLeavesRemaining()));

		return employeeDTO;
	}

	private HolidayDTO holidayEntityToDTO (Holiday holiday) {
		String NEW_FORMAT = "MMM dd, yyyy";

		HolidayDTO holidayDTO = new HolidayDTO();
		holidayDTO.setId(holiday.getId());
		holidayDTO.setDescription(holiday.getDescription());
		
		DateFormat formatter = new SimpleDateFormat(NEW_FORMAT);
		holidayDTO.setDate(formatter.format(holiday.getDate()));
		return holidayDTO;
	}

	@Override
	@Transactional
	public void newApplication(ApplicationDTO newApplication) throws MessagingException {
		Application applicationToSubmit = applicationDTOToApplicationEntity(newApplication);
		int insertedId = applicationDAO.add(applicationToSubmit);

		//Entry in EntitiesPendingApproval
		Calendar iterator = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		to.setTime(applicationToSubmit.getToDate());
		to.add(Calendar.DATE, 1);
		
		for(iterator.setTime(applicationToSubmit.getFromDate()); iterator.before(to); iterator.add(Calendar.DATE, 1)) {
			EntitiesPendingApproval e = new EntitiesPendingApproval();
			e.setApplication(applicationToSubmit);
			e.setDate(iterator.getTime());
			e.setEmployee(applicationToSubmit.getEmployee());
			entitiesPendingApprovalDAO.add(e);
		}
		
		
		//Mail to manager
		String signature = messageSource.getMessage("email-signature", null, Locale.US);
		String applicationName = messageSource.getMessage("application-name", null, Locale.US);
		
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
		/*SimpleMailMessage email = new SimpleMailMessage();*/
		Employee employee = employeeDAO.findById(Integer.parseInt(newApplication.getEmpCode()));
		email.setTo(employee.getManager().getEmail());
		email.setSubject(applicationName + " | Application Recieved");

		String message = "<html><body>" + "Dear <b>" + employee.getManager().getName() + "</b>,<br /><br />";
		message = message + "Your team member " + employee.getName() + " has submitted an application (ID: " + insertedId + ")." +
				"<br /><br />" + 
				"You are requested to please acknowledge the same.<br /><br />" +
				signature + 
				"</body></html>";
		email.setText("", message);
		email.setFrom("no-reply@openhrm.io");
		mailSender.send(msg);
	}

	private Application applicationDTOToApplicationEntity(ApplicationDTO dto){
		Application retApplication = new Application();
		retApplication.setEmployee(employeeDAO.findById(Integer.parseInt(dto.getEmpCode())));
		retApplication.setApplicationReason(dto.getApplicationReason().trim());
		retApplication.setAppliedOn(new Date());

		DateFormat formatter = new SimpleDateFormat("EEEEE, dd MMMMM, yyyy");
		try {
			retApplication.setFromDate(formatter.parse(dto.getFromDate()));
		} catch (ParseException e) {
			retApplication.setFromDate(null);
			log.error("Error in formatting date: " + dto.getFromDate());
		}
		
		try {
			retApplication.setToDate(formatter.parse(dto.getToDate()));
		} catch (ParseException e) {
			retApplication.setToDate(null);
			log.error("Error in formatting date: " + dto.getToDate());
		}
		
		retApplication.setType(ApplicationTypes.values()[Integer.parseInt(dto.getApplicationType())]);
		retApplication.setStatus(ApplicationStatus.PENDING);
		retApplication.setManager(employeeDAO.findById(Integer.parseInt(dto.getEmpCode())).getManager());
		return retApplication;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ApplicationDTO> listAllApplications(int empCode, ApplicationTypes applicationType) {
		List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
		for(Application application:applicationDAO.listApplicationsByEmployee(employeeDAO.findById(empCode), applicationType))
			applicationsList.add(applicationEntityToDTO(application));
		return applicationsList;
	}

	private ApplicationDTO applicationEntityToDTO(Application application) {
		ApplicationDTO applicationDTO = new ApplicationDTO();
		applicationDTO.setEmpCode(Integer.toString(application.getEmployee().getEmpCode()));
		applicationDTO.setId(Integer.toString(application.getId()));
		if(application.getManager() != null)
			applicationDTO.setMgrCode(Integer.toString(application.getManager().getEmpCode()));
		else
			applicationDTO.setMgrCode("&nbsp;");
		applicationDTO.setEmpName(application.getEmployee().getName());
		switch(application.getType()) {
		case LEAVE:
			applicationDTO.setApplicationType("L");
			break;
		case LEAVE_AGAINST_COMPENSATORY_OFF:
			applicationDTO.setApplicationType("LAC");
			break;
		case COMPENSATORY_OFF:
			applicationDTO.setApplicationType("CO");
			break;
		case WORK_FROM_HOME:
			applicationDTO.setApplicationType("WFH");
			break;
		}
		applicationDTO.setApplicationReason(application.getApplicationReason());
		String NEW_FORMAT = "MMM dd, yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NEW_FORMAT);

		applicationDTO.setFromDate(simpleDateFormat.format(application.getFromDate()).toString());
		applicationDTO.setToDate(simpleDateFormat.format(application.getToDate()).toString());

		applicationDTO.setAppliedOn(simpleDateFormat.format(application.getAppliedOn()).toString());
		applicationDTO.setApplicationStatus(application.getStatus().getDescription());
		applicationDTO.setRejectReason(application.getRejectionReason());
		if(application.getAcknowledgedOn() != null)
			applicationDTO.setAcknowledgedOn(application.getAcknowledgedOn().toString());
		else
			applicationDTO.setAcknowledgedOn("&nbsp;");
		return applicationDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeDTO getEmployee(int empCode) {
		return employeeEntityToDTO(employeeDAO.findById(empCode));
	}

	@Override
	@Transactional(readOnly = true)
	public int getCompensatoryOffBalance(int empCode) {
		return compensatoryOffDAO.getValidUnusedCompensatoryOffs(employeeDAO.findById(empCode), new Date()).size();
	}	

	@Override
	@Transactional
	public void deleteApplication(int id) throws InvalidRequestException {
		Application applicationToDelete = applicationDAO.findById(id);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
		if((applicationToDelete != null) && (applicationToDelete.getEmployee().getEmpCode() == empCode)) {
			if(applicationToDelete.getStatus() == ApplicationStatus.PENDING) {
				applicationDAO.delete(id);
				entitiesPendingApprovalDAO.deleteAllByApplication(id);
			}
			else 
				throw new InvalidRequestException("You can not delete an application once acknowlegded by manager.");	
		}
		else
			throw new AuthorizationFaliureException();
	}

	@Override
	@Transactional
	public void forgotPassword(int empCode) throws MessagingException, NoSuchElementException {
		PasswordReset newRequest = new PasswordReset();
		Employee emp = employeeDAO.findById(empCode);
		if(emp == null)
			throw new NoSuchElementException("Invalid Employee Code");
		newRequest.setEmployee(emp);
		newRequest.setTimestamp(new Date());
		String rand = null;
		rand = RandomStringUtils.randomAlphanumeric(20);
		newRequest.setAuthenticationCode(DigestUtils.sha1Hex((empCode + rand + emp.getEmail())));

		passwordResetDAO.deleteAllByEmployee(empCode);
		passwordResetDAO.add(newRequest);
		String hostIp = messageSource.getMessage("host-ip", null, Locale.US);
		String hostPort = messageSource.getMessage("host-port", null, Locale.US);
		String signature = messageSource.getMessage("email-signature", null, Locale.US);
		String applicationName = messageSource.getMessage("application-name", null, Locale.US);
		String resetLink = "http://" + hostIp + ":" + hostPort + "/reset?code=" + newRequest.getAuthenticationCode() + DigestUtils.sha1Hex(Integer.toString(empCode));
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
		email.setTo(emp.getEmail());
		email.setSubject(applicationName + " | Password Assistance");
		String message = "<html><body>" + "Dear <b>" + emp.getName() + "</b>,<br /><br />";
		message = message + "To initiate the password reset process for your account, " +
				"click the link below: <br /><br />" + 
				"<a href = '" + resetLink + "'>" + resetLink + "</a><br /><br />" +  
				"If clicking the link above doesn't work, please copy and paste the URL in a new browser window instead. <br /><br />" + 
				"<p>If you've received this mail in error, it's likely that another user entered your email address by mistake " + 
				"while trying to reset a password. <br />If you didn't initiate the request," + 
				"you don't need to take any further action and can safely disregard this email.</p><br /><br />" + 
				signature + 
				"</body></html>";
		email.setText("", message);
		mailSender.send(msg);
	}

	@Override
	@Transactional
	public boolean resetPassword(String authCode) throws MessagingException {
		Employee emp = passwordResetDAO.findByAuthCode(authCode.substring(0, 40));
		if(emp == null)
			return false;
		else if(!DigestUtils.sha1Hex(Integer.toString(emp.getEmpCode())).equals(authCode.substring(40)))
			return false;
		else {
			String newPass = RandomStringUtils.randomAlphanumeric(8);
			emp.setPassword(DigestUtils.sha1Hex(newPass));
			System.out.println(newPass);
			employeeDAO.edit(emp);
			passwordResetDAO.deleteAllByEmployee(emp.getEmpCode());
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
			email.setTo(emp.getEmail());
			email.setSubject("Employee Portal | Password Assistance");
			String message = "<html><body>" + "Dear <b>" + emp.getName() + "</b>,<br /><br />";
			message = message + "As per your request, " +
					"your password has been reset.<br /><br />" + 
					"<div style = 'margin=left: 50px'>Password: <b>" + newPass + "</b></div>" +  
					"Please login using the above password and change it immediately.<br /><br />" + 
					"Sincerely,<br />" + 
					"Employee Portal" +
					"</body></html>";
			email.setText("", message);
			mailSender.send(msg);
			return true;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ArrayList<String>> getReport(ApplicationTypes type,
			int empCode, Date fromDate, Date toDate) {
		ArrayList<ArrayList<String>> employeeHistory = new  ArrayList<ArrayList<String>>();
		switch(type) {
		case LEAVE:
			for(Leave leave: leaveDAO.getEmployeeReport(empCode, fromDate, toDate)) {
				DateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(formatter.format(leave.getDate()));
				temp.add(leave.getLeaveType().getDescription());
				temp.add((leave.getCancelDate() == null)? "": "Cancelled on " + formatter.format(leave.getCancelDate()));
				employeeHistory.add(temp);
			}
			break;
		case WORK_FROM_HOME:
			for(WorkFromHome wfh: workFromHomeDAO.getEmployeeReport(empCode, fromDate, toDate)) {
				ArrayList<String> temp = new ArrayList<String>();
				DateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
				temp.add(formatter.format(wfh.getDate()));
				temp.add("Work from home");
				temp.add((wfh.getCancelDate() == null)? "": "Cancelled on " + formatter.format(wfh.getCancelDate()));
				employeeHistory.add(temp);
			}
			break;
		case COMPENSATORY_OFF:
			for(CompensatoryOff compOff: compensatoryOffDAO.getEmployeeReport(empCode, fromDate, toDate)) {
				ArrayList<String> temp = new ArrayList<String>();
				DateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
				temp.add(formatter.format(compOff.getAppliedAgainst()));
				temp.add("Compensatory Off");
				Date now = DateUtils.truncate(new Date(), Calendar.DATE);
				if(compOff.getLeave() == null) {
					if(now.compareTo(compOff.getAppliedAgainst()) >=0) {
						Calendar c = Calendar.getInstance();
						c.setTime(compOff.getAppliedAgainst());
						c.add(Calendar.DATE, 90);
						temp.add("Expiring on " + formatter.format(c.getTime()));
					}
					else {
						temp.add("Expired");
					}
				} else {
					temp.add("Consumed on " + formatter.format(compOff.getLeave().getDate()));
				}
				employeeHistory.add(temp);
			}
			break;
		default:
			break;
		}
		return employeeHistory;

	}
}
