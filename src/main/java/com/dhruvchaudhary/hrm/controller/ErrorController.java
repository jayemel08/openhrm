package com.dhruvchaudhary.hrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String redirector(){
		return "redirect:/error";
	}
	
	@RequestMapping(value = "bad-request", method=RequestMethod.GET)
	public String badRequest(){
		return "error-pages/bad-request";
	}
	
	
}
