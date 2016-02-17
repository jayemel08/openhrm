package com.dhruvchaudhary.hrm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhruvchaudhary.hrm.customValidators.validators.EmployeeValidator;
import com.dhruvchaudhary.hrm.dto.HolidayDTO;
import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;
import com.dhruvchaudhary.hrm.service.interfaces.EmployeeService;
import com.dhruvchaudhary.hrm.service.interfaces.HRService;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;

@Controller
@RequestMapping(value = "hr")
public class HRController {
	
	private Logger log = Logger.getLogger(HRController.class);
	
	@Autowired
	private HRService hrServiceImpl;
	
	@Autowired
	private EmployeeService employeeServiceImpl;
	
	@Autowired
	private EmployeeValidator employeeValidator;
	
	@RequestMapping(method=RequestMethod.GET)
	public String showHomePage(Model model){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String employeename = "";
		int empCode = Integer.parseInt(((UserDetails)principal).getUsername());
		if (principal instanceof UserDetails) {
		  employeename = employeeServiceImpl.getEmployee(empCode).getName();
		} 
		model.addAttribute("username", employeename);
		int onRoles = hrServiceImpl.getEmployeeCount(EmployeeStatus.ON_ROLES);
		int onNotice = hrServiceImpl.getEmployeeCount(EmployeeStatus.IN_NOTICE_PERIOD);
		int onProbation = hrServiceImpl.getEmployeeCount(EmployeeStatus.ON_PROBATION);
		
		model.addAttribute("onRoles", onRoles);
		model.addAttribute("onNotice", onNotice);
		model.addAttribute("onProbation", onProbation);
		model.addAttribute("total", onProbation + onNotice + onRoles);
		model.addAttribute("leaveList", hrServiceImpl.listEmployeesOnLeave(new Date()));
		model.addAttribute("wfhList", hrServiceImpl.listEmployeesOnWFH(new Date()));
		return "hr/index";
	}
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String redirector(){
		return "redirect:/hr";
	}
	
	@RequestMapping(value = "addEmployee", method=RequestMethod.POST)
	@Transactional
	public String addEmployee(@Valid @ModelAttribute("newEmployee") EmployeeDTO newEmployee, BindingResult result, Model model) {
		newEmployee.setLeavesRemaining(newEmployee.getLeavesTotal());
		
		if(!result.hasErrors())
			employeeValidator.validate(newEmployee, result); // for unique constraints' checking
		if(!result.hasErrors()) {
			try {
				hrServiceImpl.addEmployee(newEmployee);
				model.addAttribute("status", "success");
				return "redirect:/hr/addEmployee";
			} catch (DataAccessException e) {
				model.addAttribute("message", "There was a problem accessing the database. Please try again later");
				log.error(e.toString());
				return "error-pages/generic-message";
			} catch (MessagingException e) {
				model.addAttribute("status", "mail_error");
				log.error(e.toString());
				return "redirect:/hr/addEmployee";
			}
		}
		else {
			/*model.addAttribute("q", result.getErrorCount());*/
			model.addAttribute("listOfManagers", hrServiceImpl.listAllManagers());
			model.addAttribute("statuses", EmployeeStatus.values());
			List<String> roleList = new ArrayList<String>();
			for(EmployeeAdditionalRoles role:hrServiceImpl.listAllAdditionalRoles())
				roleList.add(role.getDescription().substring(5));
			model.addAttribute("additionalRoles", roleList);
			return "hr/addEmployee";
		}
		
    }
	
	@RequestMapping(value = "addEmployee", method=RequestMethod.GET)
	public String addEmployeeShowForm(Model model) {
		EmployeeDTO newEmployee = new EmployeeDTO();
		newEmployee.setStatusCode(Integer.toString(EmployeeStatus.ON_ROLES.getId()));
		List<String> roleList = new ArrayList<String>();
		for(EmployeeAdditionalRoles role:hrServiceImpl.listAllAdditionalRoles())
			roleList.add(role.getDescription().substring(5));
		model.addAttribute("listOfManagers", hrServiceImpl.listAllManagers());
		model.addAttribute("newEmployee", newEmployee);
		model.addAttribute("statuses", EmployeeStatus.values());
		model.addAttribute("additionalRoles", roleList);
	      return "hr/addEmployee";
    }
	
	@RequestMapping(value = "deleteEmployee")
	public String deleteEmployee(@RequestParam(value = "id", required = true) int empCode, Model model) {
		try {
			hrServiceImpl.deleteEmployee(empCode);
		} catch(DataIntegrityViolationException e) {
			log.error(e.toString());
			model.addAttribute("errorCode", "1");
		}
        return "redirect:/hr/employees";
    }
	
	@RequestMapping(value = "editEmployee", method = RequestMethod.GET)
	public String editEmployeeShowForm(@RequestParam(value = "id", required = true) int empCode, Model model) {
		model.addAttribute("updatedEmployee", hrServiceImpl.getEmployeeByEmployeeCode(empCode));
		List<String> roleList = new ArrayList<String>();
		for(EmployeeAdditionalRoles role:hrServiceImpl.listAllAdditionalRoles())
			roleList.add(role.getDescription().substring(5));
		model.addAttribute("listOfManagers", hrServiceImpl.listAllManagers());
		model.addAttribute("statuses", EmployeeStatus.values());
		model.addAttribute("additionalRoles", roleList);
        return "hr/editEmployee";
    }
	
	@RequestMapping(value = "editEmployee", method = RequestMethod.POST)
	@Transactional
	public String editEmployee(@Valid @ModelAttribute("updatedEmployee") EmployeeDTO updatedEmployee, BindingResult result, Model model) {
		if(!result.hasErrors())
			hrServiceImpl.editEmployee(updatedEmployee);
		else {
			List<String> roleList = new ArrayList<String>();
			for(EmployeeAdditionalRoles role:hrServiceImpl.listAllAdditionalRoles())
				roleList.add(role.getDescription().substring(5));
			model.addAttribute("listOfManagers", hrServiceImpl.listAllManagers());
			model.addAttribute("statuses",EmployeeStatus.values());
			model.addAttribute("additionalRoles", roleList);
			model.addAttribute("q", result.getErrorCount());
	        return "hr/editEmployee";
		}
        return "redirect:/hr/employees";
    }
	
	@RequestMapping(value = "employees")
	public String listEmployee(Model model) {
		model.addAttribute("employeeList", hrServiceImpl.listAllEmployees());
        return "hr/employee";
    }
	
	@RequestMapping(value = "listEmployees")
	public @ResponseBody String getEmployeesList(@RequestParam(value = "query") String query, Model model) {
		String returnString = "{\"employees\":[";
		for(EmployeeDTO employee:hrServiceImpl.listAllEmployees(query)) {
			returnString = returnString + "{" +
							"\"id\":\"" + employee.getEmpCode() + "\"," +
							"\"name\":\"" + employee.getName() + "\"," +
							"\"mgrname\":\"" + employee.getMgrName() + "\"," +
							"\"status\":\"" + employee.getStatus() + "\"," +
							"\"email\":\"" + employee.getEmail() + "\"" +
							"},"; 
		}
		returnString = returnString.substring(0, returnString.length() - 1) + "]}";
		System.out.println(returnString);
        return returnString;
    }
	
	@RequestMapping(value = "viewEmployee")
	public @ResponseBody String viewEmployee(@RequestParam(value = "empCode") String empCode, Model model) {
		EmployeeDTO employee =  hrServiceImpl.getEmployeeByEmployeeCode(Integer.parseInt(empCode));
		String returnString = "{\"employees\":[";
		returnString = returnString + "{" +
						"\"id\":\"" + employee.getEmpCode() + "\"," +
						"\"name\":\"" + employee.getName() + "\"," +
						"\"mgrname\":\"" + employee.getMgrName() + "\"," +
						"\"status\":\"" + employee.getStatus() + "\"," +
						"\"email\":\"" + employee.getEmail() + "\"," +
						"\"doj\":\"" + employee.getDoj() + "\"," +
						"\"leavesTotal\":\"" + employee.getLeavesTotal() + "\"," +
						"\"leavesRemaining\":\"" + employee.getLeavesRemaining() + "\"," +
						"\"managerCode\":\"" + employee.getMgrCode() + "\"," +
						"\"roles\":\"";
		for(String role: employee.getRoles()){
			returnString = returnString + role + "&nbsp;";						
		}
		returnString = returnString + "\"}]}"; 	
		System.out.println(returnString);
        return returnString;
    }
	
	@RequestMapping(value = "reports")
	public String showReports(@RequestParam(value = "mgr", required = false) String mgrCode, Model model) {
        return "hr/reports";
    }
	
	@RequestMapping(value = "consolidatedReport")
	public String downloadConsolidatedReports(@RequestParam(value = "fromDate") String from, @RequestParam(value = "toDate") String to, Model model) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			model.addAttribute("message", "We are unable to process your request. Please try again");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
		model.addAttribute("data", hrServiceImpl.generateConsolidatedReport(fromDate, toDate));
        return "consolidatedExcel";
    }
	
	@RequestMapping(value = "teamWiseReport")
	public String downloadTeamWiseReports(@RequestParam(value = "mgrCode") String mgrCode,@RequestParam(value = "fromDate") String from, @RequestParam(value = "toDate") String to, Model model) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			model.addAttribute("message", "We are unable to process your request. Please try again");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
		model.addAttribute("data", hrServiceImpl.generateManagerWiseReport(Integer.parseInt(mgrCode), fromDate, toDate));
		model.addAttribute("mgrCode", mgrCode);
        return "teamExcel";
    }
	
	@RequestMapping(value = "employeeWiseReport")
	public String downloadEmployeeWiseReports(@RequestParam(value = "empCode") String empCode, @RequestParam(value = "fromDate") String from, @RequestParam(value = "toDate") String to, Model model) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			model.addAttribute("message", "We are unable to process your request. Please try again");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
		model.addAttribute("leaveData", hrServiceImpl.getEmployeeLeaveReport(Integer.parseInt(empCode), fromDate, toDate));
		model.addAttribute("compOffData", hrServiceImpl.getEmployeeCompOffReport(Integer.parseInt(empCode), fromDate, toDate));
		model.addAttribute("wfhData", hrServiceImpl.getEmployeeWFHReport(Integer.parseInt(empCode), fromDate, toDate));
		try {
			model.addAttribute("empName", employeeServiceImpl.getEmployee(Integer.parseInt(empCode)).getName());
		} catch (NumberFormatException e) {
			model.addAttribute("message", "We are unable to process your request. Please try again");
			/*log.error(e.toString());*/
			return "error-pages/generic-message";
		} catch (NullPointerException e) {
			model.addAttribute("message", "We are unable to process your request. Please try again");
			/*log.error(e.toString());*/
			return "error-pages/generic-message";
		}
        return "employeeExcel";
    }
	
	@RequestMapping(value = "employeeWise")
	public @ResponseBody String getEmployeeReport(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "empCode", required = false) String empCode, @RequestParam(value = "fromDate", required = false) String from, @RequestParam(value = "toDate", required = false) String to) {
		Date fromDate = null;
		Date toDate = null;
		int applicationType = 0;
		int employeeCode = 0;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
			applicationType = Integer.parseInt(type);
			employeeCode = Integer.parseInt(empCode);
		} catch (ParseException e) {
			/*log.error(e.toString());*/
			return "";
		}catch (NumberFormatException e) {
			/*log.error(e.toString());*/
			return "";
		}
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for(ArrayList<String> e:hrServiceImpl.generateEmployeeWiseReport(ApplicationTypes.values()[applicationType], employeeCode, fromDate, toDate)) {
			JSONObject json = new JSONObject();
			json.put("date", e.get(0));
			json.put("type", e.get(1));
			json.put("status", e.get(2));
			jsonArray.add(json);
		}
		jsonObj.put("details", jsonArray);
		return jsonObj.toString();
    }
	
	@RequestMapping(value = "teamwise")
	public @ResponseBody String getTeamReport(@RequestParam(value = "mgrCode", required = false) String mgrCode, @RequestParam(value = "fromDate", required = false) String from, @RequestParam(value = "toDate", required = false) String to) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			/*log.error(e.toString());*/
			return "";
		}
		JSONArray jsonArray = new JSONArray();
		for(Entry<Integer, ArrayList<String>> e:hrServiceImpl.generateManagerWiseReport(Integer.parseInt(mgrCode), fromDate, toDate).entrySet()) {
			JSONObject json = new JSONObject();
			json.put("id", e.getKey());
			json.put("empname", e.getValue().get(0));
			json.put("paidleaves", e.getValue().get(1));
			json.put("lwp", e.getValue().get(2));
			json.put("compoff", e.getValue().get(3));
			json.put("grantedcompoff", e.getValue().get(4));
			json.put("wfh", e.getValue().get(5));
			jsonArray.add(json);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("employees", jsonArray);
        return jsonObj.toString();
    }
	
	@RequestMapping(value = "consolidated")
	public @ResponseBody String getConsolidatedReport(@RequestParam(value = "fromDate", required = false) String from, @RequestParam(value = "toDate", required = false) String to) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			/*log.error(e.toString());*/
			return "";
		}
		JSONArray jsonArray = new JSONArray();
		for(Entry<Integer, ArrayList<String>> e:hrServiceImpl.generateConsolidatedReport(fromDate, toDate).entrySet()) {
			JSONObject json = new JSONObject();
			json.put("id", e.getKey());
			json.put("empname", e.getValue().get(0));
			json.put("paidleaves", e.getValue().get(1));
			json.put("lwp", e.getValue().get(2));
			json.put("compoff", e.getValue().get(3));
			json.put("grantedcompoff", e.getValue().get(4));
			json.put("wfh", e.getValue().get(5));
			jsonArray.add(json);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("employees", jsonArray);
        return jsonObj.toString();
    }
	
	@RequestMapping(value = "addHoliday", method = RequestMethod.GET) 
	public String showNewHolidayform(Model model) {
		HolidayDTO newCalenderLeaveDTO = new HolidayDTO();
		model.addAttribute("newCalenderLeave", newCalenderLeaveDTO);
		return "hr/addHoliday";
	}
	
	@RequestMapping(value = "addHoliday", method = RequestMethod.POST) 
	public String addHoliday(@Valid @ModelAttribute("newCalenderLeave") HolidayDTO newCalenderLeaveDTO, BindingResult result, Model model) {
		if(!result.hasErrors()){
			hrServiceImpl.addCalenderLeaves(newCalenderLeaveDTO);
			model.addAttribute("message", "Holiday added.");
			return "redirect:/hr/addHoliday";
		}
		return "hr/addHoliday";
	}
	
	@RequestMapping(value = "holidays") 
	public String listHolidays(Model model) {
		List<HolidayDTO> listOfCalenderLeaves = hrServiceImpl.listAllCalenderLeaves();
		model.addAttribute("listOfCalenderLeaves", listOfCalenderLeaves);
		return "hr/holidays";
	}
	
	@RequestMapping(value = "deleteHoliday") 
	public String deleteHoliday(@RequestParam(value = "id", required = true) int leaveId, Model model) {
		hrServiceImpl.deleteCalenderLeaves(leaveId);
		model.addAttribute("message", "Holiday deleted.");
		return "redirect:/hr/holidays";
	}
	
	@RequestMapping(value = "managersList")
	public @ResponseBody String getListOfManagers() {
		
		JSONArray jsonArray = new JSONArray();
		for(EmployeeDTO e:hrServiceImpl.listAllManagers()) {
			JSONObject json = new JSONObject();
			json.put("id", e.getEmpCode());
			json.put("name", e.getName());
			jsonArray.add(json);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("managers", jsonArray);
        return jsonObj.toString();
    }
	
	@RequestMapping(value = "cancellations", method = RequestMethod.GET)
	public String cancellationsForm(Model model) {
		return "hr/cancellations";
	}

	@RequestMapping(value = "cancellations", method = RequestMethod.POST)
	public String cancellationsList(@RequestParam(value="empCode", required = true) String empCode, @RequestParam(value="applicationId", required = true) String applicationId, Model model) {
		try {
			ArrayList<ArrayList<String>> dataList = hrServiceImpl.getEmployeeApplicationData(Integer.parseInt(empCode), Integer.parseInt(applicationId));
			if(dataList.size()>0)
				model.addAttribute("data", dataList);
			else
				model.addAttribute("message", "Employee code and Application ID combination invalid.");
		} catch(NumberFormatException e) {
			model.addAttribute("message", "Invalid employee code or application id");
			/*log.error(e.toString());*/
		}
		return "hr/cancellations";
	}
	
	@RequestMapping(value = "cancel", method = RequestMethod.POST)
	public @ResponseBody String cancelRequest(@RequestParam(value="id", required = true) String id, @RequestParam(value="type", required = true) String type, Model model) {
		try {
			hrServiceImpl.cancel(Integer.parseInt(id), Integer.parseInt(type));
		} catch (NumberFormatException e) {
			/*log.error(e.toString());*/
			return "Invalid Request";
		} catch (InvalidRequestException e) {
			log.error(e.toString());
			return e.toString();
		}
		return "Done";
	}
}
