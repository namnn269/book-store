package com.nam.utils;

import javax.servlet.http.HttpServletRequest;

public class UrlFromUser {
	public static String getUrl(HttpServletRequest http) {
		return "http://" + http.getServerName() + ":" + http.getServerPort() + http.getContextPath();
	}
}
