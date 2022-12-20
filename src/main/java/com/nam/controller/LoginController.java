package com.nam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	/* Hiển trị trang login */
	@GetMapping
	public String login(@RequestParam(value = "param", defaultValue = "") String param, Model model) {
		model.addAttribute("param",param);
		return "signin-up/loginform";
	}
	
	/* Xử lý nếu không có quyền truy vập vào admin */
	@GetMapping("/access-denied-exception")
	public String handlerException() {
		
		return "view/error/access-denied-exception";
	}

}
