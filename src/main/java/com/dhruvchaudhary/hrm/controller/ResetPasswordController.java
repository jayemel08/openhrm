package com.dhruvchaudhary.hrm.controller;

import javax.mail.MessagingException;

import com.dhruvchaudhary.hrm.service.interfaces.EmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="reset")
public class ResetPasswordController {
	
	@Autowired
	private EmployeeService employeeServiceImpl;
	private Logger log = Logger.getLogger(ResetPasswordController.class);
	
	
	@RequestMapping(method=RequestMethod.GET )
	public String showForm(@RequestParam(value = "code", required = true) String authCode, Model model){
		try {
			employeeServiceImpl.resetPassword(authCode);
			model.addAttribute("message", "Your password has been reset and mailed to you.");
		} catch (MessagingException e) {
			model.addAttribute("message", "Unable to send emails. Please contact HR Team");
			log .error(e.toString());
		} catch (DataAccessException e) {
			model.addAttribute("message", "There was a problem accessing the database. Please try again later. If this repeats, please contact HR Team.");
			log.error(e.toString());
		}
		return "error-pages/generic-message";
	}
	
}
