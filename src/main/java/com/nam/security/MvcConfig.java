package com.nam.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/signin-up/loginform");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

//		registry.addResourceHandler("/img/**", "/css/**","/js/**")
//				.addResourceLocations("classpath:/static/img/",
//				"classpath:/static/css/","classpath:/static/js/");
		
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/static/");

	}

}
