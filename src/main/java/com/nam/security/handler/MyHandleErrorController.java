package com.nam.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyHandleErrorController implements ErrorController {
	
	/* Khi xảy ra lỗi gọi đến phương thức này và trả về trang lỗi */
	@RequestMapping("/error")
	public String handleError(HttpServletRequest http) {
//		Object status = http.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//		if (status != null)
//			return "view/error/common-error";
		return "view/error/common-error";
	}
}
