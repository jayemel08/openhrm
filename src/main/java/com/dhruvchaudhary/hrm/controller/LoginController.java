package com.dhruvchaudhary.hrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="login")
public class LoginController {
	
	@RequestMapping(value = "/", method=RequestMethod.GET )
	public String redirector(Model model){
		return "redirect:/login";
	}
	
	@RequestMapping(method=RequestMethod.GET )
	public String doLogin(Model model){
		return "login/index";
	}
	
}
