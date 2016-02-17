package com.dhruvchaudhary.hrm.controller;

import javax.mail.MessagingException;

import com.dhruvchaudhary.hrm.service.interfaces.EmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dhruvchaudhary.hrm.exceptions.NoSuchElementException;

@Controller
@RequestMapping(value="forgotPassword")
public class ForgotPasswordController {
	
	private Logger log = Logger.getLogger(ForgotPasswordController.class);
	
	@Autowired
	private EmployeeService employeeServiceImpl;
	
	@RequestMapping(value = "/", method=RequestMethod.GET )
	public String redirector(Model model){
		return "redirect:/forgotPassword";
	}
	
	@RequestMapping(method=RequestMethod.GET )
	public String showForm(Model model){
		return "login/forgotPassword";
	}
	
	@RequestMapping(method=RequestMethod.POST )
	public String newRequest(@ModelAttribute("empCode") String empCode, Model model){
		try {
			employeeServiceImpl.forgotPassword(Integer.parseInt(empCode));
			model.addAttribute("message", "An email containing a confirmation link has been sent to your registred email address.");
			return "error-pages/generic-message";
		}catch(NumberFormatException e) {
			model.addAttribute("message", "Invalid Employee Code");
			log.error(e.toString() + "Invalid Employee Code : " + empCode);
		} catch (MessagingException e) {
			model.addAttribute("message", "Unable to send mail. Please contact the HR team.");
			log.error(e.toString() + ": Employee Code : " + Integer.parseInt(empCode));
			return "error-pages/generic-message";
		} catch (NoSuchElementException e) {
			model.addAttribute("message", e.toString());
		}
		return "login/forgotPassword";
		
	}
	
}
