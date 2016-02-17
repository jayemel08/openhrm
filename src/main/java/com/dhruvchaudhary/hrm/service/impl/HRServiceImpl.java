package com.dhruvchaudhary.hrm.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.dto.HolidayDTO;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.service.interfaces.HRService;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhruvchaudhary.hrm.dao.interfaces.CompensatoryOffDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeAdditionalRolesDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.GenericDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.LeaveDAO;
import com.dhruvchaudhary.hrm.dao.interfaces.WorkFromHomeDAO;
import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Holiday;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.WorkFromHome;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;
import com.dhruvchaudhary.hrm.utils.LeaveType;

@Service
public class HRServiceImpl implements HRService {

	@Autowired
	private EmployeeDAO employeeDAOImpl;

	@Autowired
	@Qualifier("calenderLeavesDAOImpl")
	private GenericDAO<Holiday> calenderLeavesDAOImpl;

	@Autowired
	private EmployeeAdditionalRolesDAO additionalRolesDAOImpl;

	@Autowired
	private LeaveDAO leaveDAOImpl;
	
	@Autowired
	private WorkFromHomeDAO workFromHomeDAOImpl;
	
	@Autowired
	private CompensatoryOffDAO compensatoryOffDAOImpl;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JavaMailSender mailSender;

	private Logger log = Logger.getLogger(HRServiceImpl.class);
	
	@Override
	@Transactional(noRollbackFor=MailException.class)
	public void addEmployee(EmployeeDTO newEmployee) throws MessagingException {
		Employee employeeToAdd = employeeDTOToEntity(newEmployee);
		String password = null;
		password = RandomStringUtils.randomAlphanumeric(8);
		employeeToAdd.setPassword(DigestUtils.sha1Hex(password));
		int newEmpCode = employeeDAOImpl.add(employeeToAdd);
		
		String hostIp = messageSource.getMessage("host-ip", null, Locale.US);
		String hostPort = messageSource.getMessage("host-port", null, Locale.US);
		String signature = messageSource.getMessage("email-signature", null, Locale.US);
		String applicationName = messageSource.getMessage("application-name", null, Locale.US);
		
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(msg, true, "UTF-8");
		/*SimpleMailMessage email = new SimpleMailMessage();*/
		Employee employee = employeeDAOImpl.findById(newEmpCode);
        email.setTo(employee.getEmail());
        email.setSubject(applicationName + " | Welcome");
        String message = "<html><body>" + "Dear <b>" + employee.getName() + "</b>,<br /><br />";
		message = message + "Welcome to " + applicationName + ". Your login credentials are: " +
				"<br /><br />" + 
				"Username: " + newEmpCode + "<br />" + 
				"Password: " + password + "<br /><br />" +
				"You can login at: <a href = 'http://" + hostIp + ":" + hostPort + "/'>" + hostIp + ":" + hostPort + "</a><br /><br />" +
				signature + 
				"</body></html>";

        email.setText("", message);
		email.setFrom("no-reply@openhrm.io");
        mailSender.send(msg);
	}

	@Override
	@Transactional
	public void editEmployee(EmployeeDTO updatedEmployee) {
		Employee employeeToUpdate = employeeDTOToEntity(updatedEmployee);
		employeeToUpdate.setPassword(employeeDAOImpl.findById(employeeToUpdate.getEmpCode()).getPassword());
		employeeDAOImpl.edit(employeeToUpdate);
	}

	@Override
	@Transactional
	public void deleteEmployee(int empCode) {
		employeeDAOImpl.delete(empCode);
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeDTO getEmployeeByEmployeeCode(int empCode) {
		return entityToEmployeeDTO(employeeDAOImpl.findById(empCode));
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listAllEmployees() {
		List<EmployeeDTO> retList = new ArrayList<EmployeeDTO>();
		try{
		for(Employee emp:employeeDAOImpl.listAll())
			retList.add(entityToEmployeeDTO(emp));
		} catch (NullPointerException e) {
			return null;
		}
		return retList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listAllEmployees(String criteria) {
		List<EmployeeDTO> retList = new ArrayList<EmployeeDTO>();
		if(criteria.trim().length() == 0)
			for(Employee emp:employeeDAOImpl.listAll())
				retList.add(entityToEmployeeDTO(emp));
		else
			for(Employee emp:employeeDAOImpl.listAll(criteria))
				retList.add(entityToEmployeeDTO(emp));
		return retList;
	}

	@Override
	@Transactional
	public void addCalenderLeaves(HolidayDTO newCalenderLeaveDTO) {
		calenderLeavesDAOImpl.add(holidayDTOToEntity(newCalenderLeaveDTO));
	}

	@Override
	@Transactional
	public void deleteCalenderLeaves(int id) {
		calenderLeavesDAOImpl.delete(id);
	}

	private Employee employeeDTOToEntity(EmployeeDTO employeeDTO) {
		Employee retEmployee = new Employee();
		Employee origEmployeeIfExists = employeeDAOImpl.findById(Integer.parseInt(employeeDTO.getEmpCode()));
		retEmployee.setEmpCode(Integer.parseInt(employeeDTO.getEmpCode()));
		retEmployee.setName(employeeDTO.getName());
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
		try {
			retEmployee.setDoj((Date)formatter.parse(employeeDTO.getDoj()));
		} catch (ParseException e) {
			retEmployee.setDoj(null);
			log.error("Error in formatting date: " + employeeDTO.getDoj());
		}
		retEmployee.setEmail(employeeDTO.getEmail());
		int origLeaveTotal = 0;
		if(origEmployeeIfExists != null)
			origLeaveTotal = origEmployeeIfExists.getLeavesTotal();
		int leaveDiff = Integer.parseInt(employeeDTO.getLeavesTotal()) - origLeaveTotal;
		retEmployee.setLeavesTotal(Integer.parseInt(employeeDTO.getLeavesTotal()));
		if(origEmployeeIfExists == null)
			retEmployee.setLeavesRemaining(retEmployee.getLeavesTotal());
		else {
			int newLeavesRemaining = origEmployeeIfExists.getLeavesRemaining() + leaveDiff;
			retEmployee.setLeavesRemaining(newLeavesRemaining>=0?newLeavesRemaining:0);
			if(newLeavesRemaining!=0) {
				if(newLeavesRemaining < 0) {
					List<Leave> empLeaveList = leaveDAOImpl.listAll(origEmployeeIfExists.getEmpCode(), LeaveType.LEAVE_PAID);
					newLeavesRemaining *= -1;
					for(int i = 0; (i<newLeavesRemaining) && (i<empLeaveList.size()); i++) {
							empLeaveList.get(i).setLeaveType(LeaveType.LEAVE_WITHOUT_PAY);
							leaveDAOImpl.edit(empLeaveList.get(i));
					}
				}
				else {
					List<Leave> empLeaveList = leaveDAOImpl.listAll(origEmployeeIfExists.getEmpCode(), LeaveType.LEAVE_WITHOUT_PAY);
					for(int i = 0; (i<newLeavesRemaining) && (i<empLeaveList.size()); i++) {
							empLeaveList.get(i).setLeaveType(LeaveType.LEAVE_PAID);
							leaveDAOImpl.edit(empLeaveList.get(i));
					}
					retEmployee.setLeavesRemaining(retEmployee.getLeavesRemaining() - empLeaveList.size());
				}
			}
		}

		if(employeeDTO.getMgrCode().length() == 0)
			retEmployee.setManager(null);
		else
			retEmployee.setManager(employeeDAOImpl.findById(Integer.parseInt(employeeDTO.getMgrCode())));
		retEmployee.setStatus(EmployeeStatus.values()[Integer.parseInt(employeeDTO.getStatusCode())]);

		List<EmployeeAdditionalRoles> roles = new ArrayList<EmployeeAdditionalRoles>();
		for(String roleDescription:employeeDTO.getRoles())
			roles.add(additionalRolesDAOImpl.getRoleByDescription("ROLE_" + roleDescription));
		retEmployee.setRoles(roles);
		return retEmployee;
	}

	private EmployeeDTO entityToEmployeeDTO(Employee entity){
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmpCode(Integer.toString(entity.getEmpCode()));
		employeeDTO.setName(entity.getName());
		employeeDTO.setEmail(entity.getEmail());
		if(entity.getManager() != null) {
			employeeDTO.setMgrName(entity.getManager().getName());
			employeeDTO.setMgrCode(Integer.toString(entity.getManager().getEmpCode()));
		}
		else
			employeeDTO.setMgrName("&nbsp;");
		DateFormat toFormat = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
		try {
			employeeDTO.setDoj(toFormat.format(entity.getDoj()).toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		//		employeeDTO.setDoj(entity.getDoj().toString());
		employeeDTO.setLeavesRemaining(Integer.toString(entity.getLeavesRemaining()));
		employeeDTO.setLeavesTotal(Integer.toString(entity.getLeavesTotal()));
		employeeDTO.setStatusCode(Integer.toString(entity.getStatus().getId()));
		employeeDTO.setStatus(entity.getStatus().getDescription());
		String[] roles = new String[entity.getRoles().size()];
		int i = 0;
		for(EmployeeAdditionalRoles role:entity.getRoles())
			roles[i++] = role.getDescription().substring(5);
		employeeDTO.setRoles(roles);
		return employeeDTO;
	}

	private Holiday holidayDTOToEntity (HolidayDTO holidayDTO) {
		Holiday holiday = new Holiday();
		holiday.setId(holidayDTO.getId());
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
		try {
			holiday.setDate((Date)formatter.parse(holidayDTO.getDate()));
		} catch (ParseException e) {
			holiday.setDate(null);
			log.error("Error in formatting date: " + holidayDTO.getDate());
		}
		holiday.setDescription(holidayDTO.getDescription());
		return holiday;
	}

	private HolidayDTO holidayEntityToDTO (Holiday holiday) {
		HolidayDTO holidayDTO = new HolidayDTO();
		holidayDTO.setId(holiday.getId());
		String NEW_FORMAT = "EEEEE, MMMMM dd, yyyy";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NEW_FORMAT);
		
		holidayDTO.setDescription(holiday.getDescription());
		holidayDTO.setDate(simpleDateFormat.format(holiday.getDate()));
		
		return holidayDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeAdditionalRoles> listAllAdditionalRoles() {
		return additionalRolesDAOImpl.listAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HolidayDTO> listAllCalenderLeaves() {
		List<HolidayDTO> listOfCalenderLeaves = new ArrayList<HolidayDTO>();
		for(Holiday leave:calenderLeavesDAOImpl.listAll()){
			listOfCalenderLeaves.add(holidayEntityToDTO(leave));
		}
		return listOfCalenderLeaves;
	}

	@Override
	@Transactional(readOnly = true)
	public int getEmployeeCount(EmployeeStatus status) {
		return employeeDAOImpl.getEmployeeCount(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listEmployeesOnLeave(Date date) {
		List<EmployeeDTO> retList = new ArrayList<EmployeeDTO>();
		for(Leave l:leaveDAOImpl.listAll(date))
			retList.add(entityToEmployeeDTO(l.getEmployee()));
		return retList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listEmployeesOnWFH(Date date) {
		List<EmployeeDTO> retList = new ArrayList<EmployeeDTO>();
		for(WorkFromHome w:workFromHomeDAOImpl.listAll(date))
			retList.add(entityToEmployeeDTO(w.getEmployee()));
		return retList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Integer, ArrayList<String>> generateConsolidatedReport(Date fromDate, Date toDate) {
		Map<Integer, ArrayList<String>> retMap = new HashMap<Integer, ArrayList<String>>();
		for(Employee e:employeeDAOImpl.listAllManagers()){
			ArrayList<String> list = new ArrayList<String>();
			Map<Integer,ArrayList<String>> leaveMap = leaveDAOImpl.getManagerWiseReport(e.getEmpCode(), fromDate, toDate);
			Map<Integer,String> coMap = compensatoryOffDAOImpl.getManagerWiseReport(e.getEmpCode(), fromDate, toDate);
			Map<Integer,String> wfhMap = workFromHomeDAOImpl.getManagerWiseReport(e.getEmpCode(), fromDate, toDate);
			list.add(e.getName());
			list.addAll(leaveMap.get(e.getEmpCode()));
			list.add(coMap.get(e.getEmpCode()));
			list.add(wfhMap.get(e.getEmpCode()));
			retMap.put(e.getEmpCode(), list);
		}
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, ArrayList<String>> generateManagerWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, ArrayList<String>> retMap = employeeDAOImpl.listAllByManager(mgrCode); 
		Map<Integer, ArrayList<String>> leaveMap = leaveDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		Map<Integer, String> coMap = compensatoryOffDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		Map<Integer, String> wfhMap = workFromHomeDAOImpl.getEmployeeWiseReport(mgrCode, fromDate, toDate);
		for(Map.Entry<Integer, ArrayList<String>> e: leaveMap.entrySet()) {
			if(retMap.containsKey(e.getKey())) { 
				retMap.get(e.getKey()).set(1, e.getValue().get(0));
				retMap.get(e.getKey()).set(2, e.getValue().get(1));
				retMap.get(e.getKey()).set(3, e.getValue().get(2));
			}
		}
		for(Map.Entry<Integer, String> e: coMap.entrySet()) {
			if(retMap.containsKey(e.getKey()))
				retMap.get(e.getKey()).set(4, e.getValue());
		}
		
		for(Map.Entry<Integer, String> e: wfhMap.entrySet()) {
			if(retMap.containsKey(e.getKey()))
				retMap.get(e.getKey()).set(5, e.getValue());
		}
		
		//add manager's report
		int l = leaveDAOImpl.listAll(mgrCode, LeaveType.LEAVE_PAID).size();
		int lwp = leaveDAOImpl.listAll(mgrCode, LeaveType.LEAVE_WITHOUT_PAY).size();
		int lc = leaveDAOImpl.listAll(mgrCode, LeaveType.LEAVE_AGAINST_COMP_OFF).size();
		int co = compensatoryOffDAOImpl.listAll(mgrCode).size();
		int wfh = workFromHomeDAOImpl.listAll(mgrCode).size();
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(employeeDAOImpl.findById(mgrCode).getName());
		list.add(Integer.toString(l));
		list.add(Integer.toString(lc));
		list.add(Integer.toString(lwp));
		list.add(Integer.toString(co));
		list.add(Integer.toString(wfh));
		
		retMap.put(mgrCode, list);
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Employee, ArrayList<Leave>> getLeaves() {
		Map<Employee, ArrayList<Leave>> retMap = new HashMap<Employee, ArrayList<Leave>>();
		for(Employee e: employeeDAOImpl.listAll()) {
			retMap.put(e, (ArrayList<Leave>) leaveDAOImpl.listAll(e.getEmpCode(), LeaveType.LEAVE_PAID));
		}
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Employee, ArrayList<CompensatoryOff>> getCompOffs() {
		Map<Employee, ArrayList<CompensatoryOff>> retMap = new HashMap<Employee, ArrayList<CompensatoryOff>>();
		for(Employee e:employeeDAOImpl.listAll())
			retMap.put(e, (ArrayList<CompensatoryOff>)compensatoryOffDAOImpl.listAll(e.getEmpCode()));
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Employee, ArrayList<WorkFromHome>> getWFH() {
		Map<Employee, ArrayList<WorkFromHome>> retMap = new HashMap<Employee, ArrayList<WorkFromHome>>();
		for(Employee e:employeeDAOImpl.listAll())
			retMap.put(e, (ArrayList<WorkFromHome>)workFromHomeDAOImpl.listAll(e.getEmpCode()));
		return retMap;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDTO> listAllManagers() {
		List<EmployeeDTO> listOfManagers = new ArrayList<EmployeeDTO>();
		for(Employee employee:employeeDAOImpl.listAllManagers()) {
			listOfManagers.add(entityToEmployeeDTO(employee));
		}
		return listOfManagers;
		
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ArrayList<String>> generateEmployeeWiseReport(ApplicationTypes type, int empCode,
																   Date fromDate, Date toDate) {
		ArrayList<ArrayList<String>> employeeHistory = new  ArrayList<ArrayList<String>>();
		switch(type) {
		case LEAVE:
			for(Leave leave: leaveDAOImpl.getEmployeeReport(empCode, fromDate, toDate)) {
				DateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(formatter.format(leave.getDate()));
				temp.add(leave.getLeaveType().getDescription());
				temp.add((leave.getCancelDate() == null)? "": "Cancelled on " + formatter.format(leave.getCancelDate()));
				employeeHistory.add(temp);
			}
			break;
		case WORK_FROM_HOME:
			for(WorkFromHome wfh: workFromHomeDAOImpl.getEmployeeReport(empCode, fromDate, toDate)) {
				ArrayList<String> temp = new ArrayList<String>();
				DateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
				temp.add(formatter.format(wfh.getDate()));
				temp.add("Work from home");
				temp.add((wfh.getCancelDate() == null)? "": "Cancelled on " + formatter.format(wfh.getCancelDate()));
				employeeHistory.add(temp);
			}
			break;
		case COMPENSATORY_OFF:
			for(CompensatoryOff compOff: compensatoryOffDAOImpl.getEmployeeReport(empCode, fromDate, toDate)) {
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

	@Override
	@Transactional(readOnly = true)
	public List<Leave> getEmployeeLeaveReport(int empCode, Date fromDate,
			Date toDate) {
		return leaveDAOImpl.getEmployeeReport(empCode, fromDate, toDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkFromHome> getEmployeeWFHReport(int empCode, Date fromDate,
			Date toDate) {
		return workFromHomeDAOImpl.getEmployeeReport(empCode, fromDate, toDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompensatoryOff> getEmployeeCompOffReport(int empCode,
			Date fromDate, Date toDate) {
		return compensatoryOffDAOImpl.getEmployeeReport(empCode, fromDate, toDate);
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ArrayList<String>> getEmployeeApplicationData(int empCode, int applicationId) {
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
		DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		for(Leave l:leaveDAOImpl.listAll(empCode, applicationId)){
			ArrayList<String> leaveData = new ArrayList<String>();
			leaveData.add(Integer.toString(l.getId()));
			leaveData.add(formatter.format(l.getDate()));
			leaveData.add(l.getLeaveType().getDescription());
			if(l.getCancelDate() == null)
				leaveData.add("&nbsp;");
			else
				leaveData.add("Cancelled");
			leaveData.add(Integer.toString(ApplicationTypes.LEAVE.getId()));
			dataList.add(leaveData);
		}
		
		for(WorkFromHome w:workFromHomeDAOImpl.listAll(empCode, applicationId)){
			ArrayList<String> wfhData = new ArrayList<String>();
			wfhData.add(Integer.toString(w.getId()));
			wfhData.add(formatter.format(w.getDate()));
			wfhData.add("Work From Home");
			if(w.getCancelDate() == null)
				wfhData.add("&nbsp;");
			else
				wfhData.add("Cancelled");
			wfhData.add(Integer.toString(ApplicationTypes.WORK_FROM_HOME.getId()));
			dataList.add(wfhData);
		}
		return dataList;
	}

	@Override
	@Transactional
	public void cancel(int id, int type) throws InvalidRequestException {
		switch(ApplicationTypes.values()[type]) {
		case LEAVE:
			Leave l = leaveDAOImpl.findById(id);
			leaveDAOImpl.cancel(id);
			switch (l.getLeaveType()) {
			case LEAVE_PAID:
				l.getEmployee().setLeavesRemaining(l.getEmployee().getLeavesRemaining() + 1);
				employeeDAOImpl.edit(l.getEmployee());
				break;
			
			case LEAVE_AGAINST_COMP_OFF:
				compensatoryOffDAOImpl.cancelLeaveAgainstCompensatoryOff(id);
				break;
			default:
				break;
			}
			break;
		case WORK_FROM_HOME:
			workFromHomeDAOImpl.cancel(id);
			break;
		default:
			throw new InvalidRequestException();
		}
		
	}
}
