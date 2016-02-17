package com.dhruvchaudhary.hrm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.service.interfaces.ManagerService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Map.Entry; 

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhruvchaudhary.hrm.dto.ApplicationDTO;
import com.dhruvchaudhary.hrm.exceptions.AuthorizationFaliureException;
import com.dhruvchaudhary.hrm.exceptions.InvalidRequestException;
import com.dhruvchaudhary.hrm.exceptions.NoSuchElementException;


@Controller
@RequestMapping(value="manager")
public class ManagerController {

	@Autowired
	ManagerService managerServiceImpl;
	private Logger log = Logger.getLogger(ManagerController.class);
	
	@RequestMapping(value = "/", method=RequestMethod.GET )
	public String redirector(Model model){
		return "redirect:/manager";
	}
	
	@RequestMapping(method=RequestMethod.GET )
	public String showHome(Model model){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int mgrCode = Integer.parseInt(((UserDetails)principal).getUsername());
		model.addAttribute("listOfTeamMembers", managerServiceImpl.listTeam(mgrCode));
		model.addAttribute("listOfTeamMembersOnLeaveToday", managerServiceImpl.listEmployeesOnLeaveToday(new Date(), mgrCode));
		model.addAttribute("listOfTeamMembersOnWFHToday", managerServiceImpl.listEmployeesOnWFHToday(new Date(), mgrCode));
		return "manager/index";
	}
	
	@RequestMapping(value = "requests", method = RequestMethod.GET)
	public String viewPendingApplications(Model model) {
		List<ApplicationDTO> listOfApplications = managerServiceImpl.listAllPendingApplications();
		Collections.sort(listOfApplications);
		model.addAttribute("list", listOfApplications);
		return "manager/requests";
	}
	
	@RequestMapping(value = "approve", method = RequestMethod.POST)
	public @ResponseBody String approveApplication(@RequestParam(value = "id", required = true) String id, Model model) {
		try {
			managerServiceImpl.approveApplication(Integer.parseInt(id));
			return "Done";
		} catch (MessagingException e) {
			log.error(e.toString());
			return "An email could not be sent.";
		} catch (DataAccessException e) {
			log.error(e.toString());
			return "There was a problem accessing the database. The request could not be serviced";
		} catch (NumberFormatException e) {
			log.error(e.toString());
			return "Invalid Request";
		} catch (AuthorizationFaliureException e) {
			log.error(e.toString());
			return e.toString();
		} catch (NoSuchElementException e) {
			log.error(e.toString());
			return e.toString();
		} catch (InvalidRequestException e) {
			log.error(e.toString());
			return e.toString();
		}
	}
	
	@RequestMapping(value = "reject", method = RequestMethod.POST)
	public @ResponseBody String rejectApplication(@RequestParam("id") String id, @RequestParam("reason") String reason, Model model) {
		
		try {
			managerServiceImpl.rejectApplication(Integer.parseInt(id), reason);
		} catch (NumberFormatException e) {
			log .error(e.toString());
			return "Invalid Request";
		} catch (AuthorizationFaliureException e) {
			log .error(e.toString());
			return e.toString();
		} catch (NoSuchElementException e) {
			log .error(e.toString());
			return e.toString();
		} catch (InvalidRequestException e) {
			log .error(e.toString());
			return e.toString();
		} catch (MessagingException e) {
			log .error(e.toString());
			return e.toString();
		}
		return "Done";
	}
	
	@RequestMapping(value = "history", method = RequestMethod.GET)
	public String viewHistory(Model model) {
		List<ApplicationDTO> listOfApplications = managerServiceImpl.listAllAcknowledgedApplications();
		Collections.sort(listOfApplications);
		model.addAttribute("list", listOfApplications);
		return "manager/history";
	}
	
	@RequestMapping(value = "reports", method = RequestMethod.GET)
	public String showReports(Model model) {
		return "manager/reports";
	}
	
	@RequestMapping(value = "getReports", method = RequestMethod.GET)
	public @ResponseBody String getReports(@RequestParam(value = "fromDate", required = false) String from, @RequestParam(value = "toDate", required = false) String to) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(from);
			toDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(to);
		} catch (ParseException e) {
			log .error(e.toString());
			return "";
		}
		
		JSONArray jsonArray = new JSONArray();
		for(Entry<Integer, ArrayList<String>> e:managerServiceImpl.generateConsolidatedReport(getCurrentEmployeeCode(), fromDate, toDate).entrySet()) {
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
		System.out.println(jsonObj.toString());
        return jsonObj.toString();
	}
	
	private int getCurrentEmployeeCode() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Integer.parseInt(((UserDetails)principal).getUsername());
	}
}
