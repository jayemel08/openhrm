package com.dhruvchaudhary.hrm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.dhruvchaudhary.hrm.customValidators.validators.ApplicationValidator;
import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import com.dhruvchaudhary.hrm.exceptions.AuthorizationFaliureException;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import com.dhruvchaudhary.hrm.service.interfaces.EmployeeService;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
/*
	@Autowired
	private MessageSource messages;*/
	
	private Logger log = Logger.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService employeeServiceImpl;

	@Autowired
	private ApplicationValidator validator;

	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String redirector(){
		return "redirect:/employee";
	}

	@RequestMapping(method=RequestMethod.GET)
	public String showHomepage(Model model, HttpServletRequest request){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empCode = Integer.parseInt(((UserDetails)principal).getUsername());

		EmployeeDTO currentEmployee = employeeServiceImpl.getEmployee(empCode);		
		if (principal instanceof UserDetails) {
			request.getSession().setAttribute("username", currentEmployee.getName());
		} 
		model.addAttribute("listOfCalenderLeaves", employeeServiceImpl.listUpcomingHolidays());
		model.addAttribute("myDetails", currentEmployee);
		model.addAttribute("compOffLeaveBalance", employeeServiceImpl.getCompensatoryOffBalance(empCode));
		/*model.addAttribute("title", messages.getMessage("application.title", null, new Locale("en_IN")));*/
		return "employee/index";
	}

	@RequestMapping(value = "changePassword", method=RequestMethod.GET)
	public String showChangePasswordForm(Model model) {
		/*EmployeeDTO updatedEmployee = new EmployeeDTO();
		updatedEmployee.setEmpCode(Integer.toString(getCurrentEmployeeCode()));
		model.addAttribute("updatedEmployee", updatedEmployee);*/
		return "employee/changePassword";
	}

	@RequestMapping(value = "changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model) {
		if(!employeeServiceImpl.changePassword(getCurrentEmployeeCode(), oldPassword, newPassword)) {
			model.addAttribute("message", "Invalid Password");
			return "employee/changePassword";
		}
		else
			return "redirect:/employee";
	}

	@RequestMapping(value = "leaves", method=RequestMethod.GET)
	public String leavesPage(Model model, HttpServletRequest request) {
		List<ApplicationTypes> leaveTypes = new ArrayList<ApplicationTypes>();
		leaveTypes.add(ApplicationTypes.LEAVE);
		leaveTypes.add(ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF);
		List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
		applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.LEAVE));
		applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF));
		Collections.sort(applicationsList);
		model.addAttribute("applicationsList", applicationsList);
		model.addAttribute("leaveTypes", leaveTypes);
		model.addAttribute("newApplication", new ApplicationDTO());
		return "employee/leaves";
	}

	@RequestMapping(value = "leaves", method=RequestMethod.POST)
	@Transactional
	public String addLeave(@ModelAttribute("newApplication") ApplicationDTO newApplication, BindingResult result, Model model) {

		try {
			if(submitApplication(ApplicationTypes.LEAVE, newApplication, result)){
				return "redirect:/employee/leaves";
			}
			else {
				model.addAttribute("q", result.getErrorCount());
				List<ApplicationTypes> leaveTypes = new ArrayList<ApplicationTypes>();
				leaveTypes.add(ApplicationTypes.LEAVE);
				leaveTypes.add(ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF);
				model.addAttribute("leaveTypes", leaveTypes);
				List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
				applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.LEAVE));
				applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.LEAVE_AGAINST_COMPENSATORY_OFF));
				Collections.sort(applicationsList);
				model.addAttribute("applicationsList", applicationsList);
				return "employee/leaves";
			}
		} catch (MessagingException e) {
			model.addAttribute("message", "Email could not be sent to manager.");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
	}

	@RequestMapping(value = "workFromHome", method=RequestMethod.POST)
	@Transactional
	public String addWFH(@ModelAttribute("newApplication") ApplicationDTO newApplication, BindingResult result, Model model) {

		try {
			if(submitApplication(ApplicationTypes.WORK_FROM_HOME, newApplication, result)){
				return "redirect:/employee/workFromHome";
			}
			else {
				model.addAttribute("q", result.getErrorCount());
				List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
				applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.WORK_FROM_HOME));
				Collections.sort(applicationsList);
				model.addAttribute("applicationsList", applicationsList);
				return "employee/workFromHome";
			}
		} catch (MessagingException e) {
			model.addAttribute("message", "Email could not be sent to manager.");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
	}

	@RequestMapping(value = "compensatoryOff", method=RequestMethod.POST)
	@Transactional
	public String addCompOff(@ModelAttribute("newApplication") ApplicationDTO newApplication, BindingResult result, Model model) {

		try {
			if(submitApplication(ApplicationTypes.COMPENSATORY_OFF, newApplication, result)){
				return "redirect:/employee/compensatoryOff";
			}
			else {
				model.addAttribute("q", result.getErrorCount());
				List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
				applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.COMPENSATORY_OFF));
				Collections.sort(applicationsList);
				model.addAttribute("applicationsList", applicationsList);
				return "employee/compensatoryOff";
			}
		} catch (MessagingException e) {
			model.addAttribute("message", "Email could not be sent to manager.");
			log.error(e.toString());
			return "error-pages/generic-message";
		}
	}

	@RequestMapping(value = "workFromHome", method=RequestMethod.GET)
	public String showWorkFromHomePage(Model model) {
		model.addAttribute("newApplication", new ApplicationDTO());
		List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
		applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.WORK_FROM_HOME));
		Collections.sort(applicationsList);
		model.addAttribute("applicationsList", applicationsList);
		return "employee/workFromHome";
	}

	@RequestMapping(value = "compensatoryOff", method=RequestMethod.GET)
	public String showCompOffPage(Model model) {
		model.addAttribute("newApplication", new ApplicationDTO());
		List<ApplicationDTO> applicationsList = new ArrayList<ApplicationDTO>();
		applicationsList.addAll(employeeServiceImpl.listAllApplications(getCurrentEmployeeCode(), ApplicationTypes.COMPENSATORY_OFF));
		Collections.sort(applicationsList);
		model.addAttribute("applicationsList", applicationsList);
		return "employee/compensatoryOff";
	}

	@RequestMapping(value = "deleteApplication", method = RequestMethod.GET)
	public String deleteApplication(@RequestParam(value = "id", required=true) String id, @RequestParam("redirect") String redirectTo, Model model) {
		try {
			employeeServiceImpl.deleteApplication(Integer.parseInt(id));
			model.addAttribute("message", "Application deleted successfully" );
		} catch (NumberFormatException e) {
			model.addAttribute("message", "Invalid application id");
			log.error(e.toString() + ": Employee code : " + getCurrentEmployeeCode());
		} catch (AuthorizationFaliureException e) {
			model.addAttribute("message", e.toString());
			log.error(e.toString() + ": Employee code : " + getCurrentEmployeeCode());
		} catch (InvalidRequestException e) {
			model.addAttribute("message", e.toString());
			log.error(e.toString() + ": Employee code : " + getCurrentEmployeeCode());
		}
		 
		switch(redirectTo) {
		case "L":
			return "redirect:/employee/leaves";
		case "C":
			return "redirect:/employee/compensatoryOff";
		case "W":
			return "redirect:/employee/workFromHome";
		default:
			return "redirect:/employee";
		}
	}
	
	@RequestMapping(value = "reports", method = RequestMethod.GET)
	public String showReportsPage() {
		return "employee/reports";
	}

	@RequestMapping(value = "getReport")
	public @ResponseBody String getReport(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "fromDate", required = false) String from, @RequestParam(value = "toDate", required = false) String to) {
		Date fromDate = null;
		Date toDate = null;
		int applicationType = 0;
		int employeeCode = 0;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
			applicationType = Integer.parseInt(type);
			employeeCode = getCurrentEmployeeCode();
		} catch (ParseException e1) {
			log.error(e1.toString() + ": Employee code : " + getCurrentEmployeeCode());
			return "";
		}catch (NumberFormatException e) {
			log.error(e.toString() + ": Employee code : " + getCurrentEmployeeCode());
			return "";
		}
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for(ArrayList<String> e:employeeServiceImpl.getReport(ApplicationTypes.values()[applicationType], employeeCode, fromDate, toDate)) {
			JSONObject json = new JSONObject();
			json.put("date", e.get(0));
			json.put("type", e.get(1));
			json.put("status", e.get(2));
			jsonArray.add(json);
		}
		jsonObj.put("details", jsonArray);
		return jsonObj.toString();
    }
	
	private boolean submitApplication(ApplicationTypes type, ApplicationDTO newApplication, BindingResult result) throws MessagingException {
		if(type != ApplicationTypes.LEAVE)
			newApplication.setApplicationType(Integer.toString(type.getId()));
		newApplication.setEmpCode(Integer.toString(getCurrentEmployeeCode()));
		if(validator.supports(newApplication.getClass()))
			validator.validate(newApplication, result);

		if(!result.hasErrors()) {
			employeeServiceImpl.newApplication(newApplication);
			return true;
		}
		else 
			return false;
	}

	private int getCurrentEmployeeCode() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Integer.parseInt(((UserDetails)principal).getUsername());
	}
}
