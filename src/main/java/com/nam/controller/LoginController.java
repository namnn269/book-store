package com.nam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@GetMapping
	public String login() {
		return "signin-up/loginform";
	}
	
	@GetMapping("/access-denied-exception")
	public String handlerException() {
		
		return "view/error/access-denied-exception";
	}

}
